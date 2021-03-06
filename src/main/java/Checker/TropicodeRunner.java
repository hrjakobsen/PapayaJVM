/*
 *     Copyright (C) 2021.  Mathias Jakobsen <m.jakobsen.1@research.gla.ac.uk>
 *
 *     SPDX-License-Identifier: BSD-3-Clause
 */

package Checker;

import CFG.GraphAnalyser;
import CFG.InstructionGraph;
import Checker.Exceptions.CheckerException;
import Checker.Extractor.CodeExtractorClassVisitor;
import JVM.JvmClass;
import JVM.JvmContext;
import JVM.JvmMethod;
import JVM.JvmMethod.AccessFlags;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.objectweb.asm.ClassReader;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Log4j2
@Command(
        name = "Tropicode",
        mixinStandardHelpOptions = true,
        description = "Checks JVM bytecode for typestate violations")
public class TropicodeRunner implements Runnable {

    @Parameters(
            index = "0",
            description =
                    "The fully qualified name of the class that contains "
                            + "the entrypoint method. Specified like \"simpleclass/Main\".")
    private String entryClass;

    @Parameters(
            index = "1",
            description =
                    "The fully qualified method signature of the entry method. Specified with"
                            + "JVM types like \"main([Ljava/lang/String;)V\"")
    private String entryMethod;

    @Option(names = "-d", description = "Display the instruction graph of the entry method")
    boolean displayGraph = false;

    private static final Set<String> ignoreDependencies =
            new HashSet<>() {
                {
                    add("sun/security/.*");
                    add("java/lang/invoke/.*");
                    add("java/nio/.*");
                    add("sun/nio/.*");
                    add("java/util/.*");
                    add("java/util/concurrent/.*");
                    add("java/security/.*");
                    add("javax/crypto/.*");
                    add("jdk/internal/.*");
                    add("java/lang/[^O].*");
                    add("java/io/.*");
                }
            };

    public static void main(String[] args) {
        int exitCode = new CommandLine(new TropicodeRunner()).execute(args);
        System.exit(exitCode);
    }

    private static JvmClass parseClass(String classname, Queue<String> classesToLoad)
            throws IOException {
        log.debug("Parsing " + classname);
        ClassReader classReader = new ClassReader(classname);
        CodeExtractorClassVisitor cv = new CodeExtractorClassVisitor();
        classReader.accept(cv, ClassReader.SKIP_DEBUG);
        JvmClass klass = cv.getJvmClass();
        outer:
        for (String dependency : cv.getClassDependencies()) {
            for (String pattern : TropicodeRunner.ignoreDependencies) {
                if (dependency.matches(pattern)) {
                    continue outer;
                }
            }
            classesToLoad.add(dependency);
            log.debug("Adding " + dependency);
        }
        return klass;
    }

    @Override
    public void run() {
        try {
            Queue<String> classesToLoad = new ArrayDeque<>();
            classesToLoad.add(this.entryClass);

            JvmContext ctx = new JvmContext();
            while (!classesToLoad.isEmpty()) {
                String nextClass = classesToLoad.poll();
                String compiledClassName = nextClass.replaceAll("/", ".");
                if (ctx.getClasses().containsKey(nextClass)) {
                    continue;
                }
                JvmClass klass = parseClass(compiledClassName, classesToLoad);
                ctx.getClasses().put(nextClass, klass);
            }

            JvmClass klass = ctx.getClasses().get(entryClass);
            JvmMethod m = klass.getMethods().get(entryMethod);

            if (!m.is(AccessFlags.ACC_STATIC)) {
                log.error("Entry method must be static");
                System.exit(1);
            }

            ctx.allocateFrame(null, m, new ArrayList<>());

            InstructionGraph iGraph = m.getInstructionGraph();
            iGraph.explodeGraph(ctx);

            if (displayGraph) {
                iGraph.show();
            }
            new GraphAnalyser().checkGraph(iGraph, ctx);
        } catch (CheckerException | IOException ex) {
            System.err.println(ex);
        }
    }
}
