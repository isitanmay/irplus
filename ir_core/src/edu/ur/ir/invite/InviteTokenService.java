/**  
   Copyright 2008-2011 University of Rochester

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


package edu.ur.ir.invite;

import java.io.Serializable;

/**
 * Service to deal with inviting users to the system.
 * 
 * @author Nathan Sarr
 *
 */
public interface InviteTokenService extends Serializable
{
    /**
     * Get the invite token by it's token value.
     * 
     * @param token
     */
    public InviteToken getInviteToken(String token);
    
    
    /**
     * Delete the invite token.
     * 
     * @param entity
     */
    public void delete(InviteToken entity);
}
