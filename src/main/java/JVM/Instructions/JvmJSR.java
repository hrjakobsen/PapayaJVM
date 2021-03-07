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

package JVM.Instructions;

import JVM.JvmContex;
import JVM.JvmOpCode;
import org.objectweb.asm.Label;

public class JvmJSR extends JvmOperation {
    private final Label label;

    public JvmJSR(Label label) {
        super(JvmOpCode.JSR);
        this.label = label;
    }

    @Override
    public void evaluateInstruction(JvmContex ctx) {
        throw new IllegalStateException("JSR instruction not supported");
    }
}