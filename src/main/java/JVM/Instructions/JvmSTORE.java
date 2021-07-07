/*
 *     Copyright (C) 2021.  Mathias Jakobsen <m.jakobsen.1@research.gla.ac.uk>
 *
 *     Tropicode is a Java bytecode analyser used to verify object protocols.
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

package JVM.Instructions;

import JVM.JvmContext;
import JVM.JvmOpCode;
import JVM.JvmValue;

public class JvmSTORE extends JvmOperation {

    private final int index;

    public JvmSTORE(int opcode, int index) {
        super(JvmOpCode.getFromOpcode(opcode));

        this.index = index;
    }

    @Override
    public void evaluateInstruction(JvmContext ctx) {
        JvmValue val = ctx.pop();
        ctx.storeLocal(index, val);
    }

    @Override
    public String toString() {
        return super.toString() + " " + index;
    }
}
