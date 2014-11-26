/*  This file is part of the aspect  project.
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public License
 as published by the Free Software Foundation; version 2.1
 of the License.
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.

 Copyright 2007-2014 Semochkin Vitaliy Evgenevich aka Yilativs
  
 */
package net.sf.aspect4log.text;

import net.sf.aspect4log.Log;

public interface MessageBuilderFactory {

	MessageBuilder createEnterMessageBuilder(String methodName, Log log, Object[] args);

	MessageBuilder createSuccessfulReturnMessageBuilder(String methodName, Log log, Object[] args, boolean returnsNothing,Object result);

	MessageBuilder createExceptionReturnMessageBuilder(String methodName, Log log, Object[] args, Throwable throwable,String exceptionExitTemplate);
	
	MessageBuilder createMdcTemplate(Log log, Object[] args);
	
	String buildIdent();

}
