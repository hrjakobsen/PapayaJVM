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

import CFG.GraphAnalyser;
import JVM.JvmContext;
import JVM.JvmOpCode;
import JVM.JvmValue;
import java.util.Map;

public class JvmInstanceFieldOperation extends JvmFieldOperation {
    public JvmInstanceFieldOperation(JvmOpCode opcode, String owner, String fieldName) {
        super(opcode, owner, fieldName);
    }

    @Override
    public void evaluateInstruction(JvmContext ctx, GraphAnalyser analyser) {
        switch (this.opcode) {
            case PUTFIELD:
                JvmValue value = ctx.pop();
                JvmValue.Reference obj = (JvmValue.Reference) ctx.pop();
                Map<String, JvmValue> fields = ctx.getObject(obj.getIdentifier()).getFields();
                fields.put(fieldName, value);
                break;
            case GETFIELD:
                obj = (JvmValue.Reference) ctx.pop();
                fields = ctx.getObject(obj.getIdentifier()).getFields();
                value = fields.get(fieldName);
                assert value != null;
                ctx.push(value);
                break;
            default:
                throw new IllegalStateException();
        }
    }
}
