/**
  Copyright 2008 University of Rochester

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package edu.ur.file.checksum;
import java.io.File;


/**
 * Class that can calculate a checksum.
 * 
 * @author Nathan Sarr
 *
 */
public interface ChecksumCalculator {
	
	/**
	 * Calculate the checksum.
	 * 
	 * @param f the file to check the checksum.
	 * 
	 * @return the checksum as a string.
	 */
	public String calculate(File f);
	
	/**
	 * The type of algorithm this checksum calculator 
	 * uses.
	 * 
	 * @return
	 */
	public String getAlgorithmType();

}
