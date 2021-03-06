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

package JVM;

import Checker.Typestate;

import java.util.ArrayList;
import java.util.List;

public class JvmClass {
    List<JvmMethod> methods = new ArrayList<>();
    Typestate protocol = null;

    public List<JvmMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<JvmMethod> methods) {
        this.methods = methods;
    }

    @Override
    public String toString() {
        return "JvmClass{" +
                "methods=" + methods +
                '}';
    }

    public Typestate getProtocol() {
        return protocol;
    }

    public void setProtocol(Typestate protocol) {
        this.protocol = protocol;
    }
}