/*  This file is part of the aspect4log  project.
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
package net.sf.aspect4log.slf4j;

import org.slf4j.Logger;


/**
 * This factory wraps the slf4j Logger into {@link IdentAwareLogger}
 * 
 * Use this factory if you want to add messages to log via slf4j and you want those messages 
 * to have correct ident level.
 * 
 * 
 * @author yilativs
 *
 */
public class LoggerFactory  {

	public static Logger getLogger(String name) {
		return new IdentAwareLogger(org.slf4j.LoggerFactory.getLogger(name));
	}
	
	public static Logger getLogger(Class<?> clazz) {
		return new IdentAwareLogger(org.slf4j.LoggerFactory.getLogger(clazz.getName()));
	}
}
