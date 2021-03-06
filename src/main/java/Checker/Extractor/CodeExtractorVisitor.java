/*
 *     Copyright (C) 2021.  Mathias Jakobsen <m.jakobsen.1@research.gla.ac.uk>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package Checker.Extractor;

import JVM.JvmClass;
import JVM.JvmMethod;
import org.objectweb.asm.*;

public class CodeExtractorVisitor extends ClassVisitor {
    JvmClass klass = new JvmClass();
    public CodeExtractorVisitor() {
        super(Opcodes.ASM8);
    }

    public JvmClass getJvmClass() {
        return klass;
    }

    public CodeExtractorVisitor(ClassVisitor cv) {
        super(Opcodes.ASM8, cv);
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        JvmMethod m = new JvmMethod(access, name, descriptor, signature);
        klass.getMethods().add(m);
        return new CodeExtractorMethodVisitor(
                super.visitMethod(access, name, descriptor, signature, exceptions),
                m);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible){
        return new CodeExtractorAnnotationExtractor(super.visitAnnotation(desc, visible), desc, visible, klass);
    }

}

