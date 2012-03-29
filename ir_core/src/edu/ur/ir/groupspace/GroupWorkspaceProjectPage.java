/**  
   Copyright 2008-2012 University of Rochester

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

package edu.ur.ir.groupspace;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.ur.ir.file.IrFile;
import edu.ur.order.AscendingOrderComparator;
import edu.ur.persistent.BasePersistent;

/**
 * Represents the grouping of information for a group space that can be shown 
 * to the public.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceProjectPage extends BasePersistent {
	
	// eclipse generated id
	private static final long serialVersionUID = -7955753514253869132L;

	// group workspace for this project page
	private GroupWorkspace groupWorkspace;

	//description of the project
	private String description;
	
	//indicates the page is public
	private boolean pagePublic;
	
	// pictures for the group workspace
	private Set<GroupWorkspaceProjectPageImage> images = new HashSet<GroupWorkspaceProjectPageImage>();

	/* date this record was created */
	private Timestamp createdDate;
	
	// list of members for the project page
	private Set<GroupWorkspaceProjectPageMember> members = new HashSet<GroupWorkspaceProjectPageMember>();

	/**
     * Package protected constructor 
     */
    GroupWorkspaceProjectPage(){}
    
    /**
     * Constructor 
     * 
     * @param groupWorkspace - group workspace this project page belongs to.
     * 
     * @param name - name of the group project page.
     */
    GroupWorkspaceProjectPage(GroupWorkspace groupWorkspace)
    {
    	setGroupWorkspace(groupWorkspace);
    	
    	createdDate = new Timestamp(new Date().getTime());
    }
    
    /**
     * Add a member to the group workspace project page.  The member
     * must be a user in the group workspace otherwise an illegal state
     * exception is thrown.
     * 
     * @param groupWorkspaceUser - group workspace user to add.
     */
    public GroupWorkspaceProjectPageMember addMember(GroupWorkspaceUser groupWorkspaceUser)
    {
    	if( !groupWorkspace.getUsers().contains(groupWorkspaceUser) )
    	{
    		throw new IllegalStateException("user " + groupWorkspaceUser 
    				+ " is not a member of the group workspace " + groupWorkspace );
    	}
    	int order = members.size() + 1;
    	GroupWorkspaceProjectPageMember member = new GroupWorkspaceProjectPageMember(this, groupWorkspaceUser);
    	member.setOrder(order);
    	members.add(member);
    	return member;
    }
    
    /**
     * Add an image to the group workspace project page.  
     * 
     * @param irFile - image file
     */
    public GroupWorkspaceProjectPageImage addImage(IrFile irFile)
    {
    	
    	int order = images.size() + 1;
    	GroupWorkspaceProjectPageImage image = new GroupWorkspaceProjectPageImage(this, irFile);
    	image.setImageOrder(order);
    	images.add(image);
    	return image;
    }
    
    /**
     * Get the group worspace project image by the ir file id.
     * 
     * @param id - ir file id
     * @return the image or null if the image is not found
     */
    public GroupWorkspaceProjectPageImage getImageByIrFileId(Long id)
    {
    	for(GroupWorkspaceProjectPageImage image : images)
    	{
    		if( image.getImageFile().getId().equals(id))
    		{
    			return image;
    		}
    	}
    	return null;
    }
    
    /**
     * Get the group workspace project page image by id.
     * 
     * @param imageId - id of the project page image
     * @return 
     */
    public GroupWorkspaceProjectPageImage getById(Long imageId)
    {
    	for(GroupWorkspaceProjectPageImage image : images )
    	{
    		if( image.getId().equals(imageId))
    		{
    			return image;
    		}
    	}
    	
    	return null;
    }
    
    /**
     * Remove the image from the list of images.  This also updates the order 
     * of all images in the list.
     * 
     * @param image
     * @return true if the image was removed
     */
    public boolean remove(GroupWorkspaceProjectPageImage image)
    {
    	int order = image.getOrder();
    	boolean removed = false;
        removed = images.remove(image);	
        if( removed )
        {
        	for(GroupWorkspaceProjectPageImage anImage :images)
        	{
        	    if( anImage.getOrder() > order )
        	    {
        	    	anImage.setImageOrder(anImage.getOrder() - 1 );
        	    }
        	}
        }
        return removed;
    }
    
    
    /**
     * Get the group workspace project page member using the group workspace user.
     * 
     * @param groupWorkspaceUser - workspace user to get.
     * @return group workspace project page member.
     */
    public GroupWorkspaceProjectPageMember getMember(GroupWorkspaceUser groupWorkspaceUser)
    {
    	for(GroupWorkspaceProjectPageMember member : members)
    	{
    		if( member.getGroupWorkspaceUser().equals(groupWorkspaceUser))
    		{
    			return member;
    		}
    	}
    	return null;
    }
    
    /**
     * Remove the member from the list of members.  This also updates the order 
     * of all members in the list.
     * 
     * @param user
     */
    public boolean removeMember(GroupWorkspaceProjectPageMember member)
    {
    	int order = member.getOrder();
    	boolean removed = false;
        removed = members.remove(member);	
        if( removed )
        {
        	for(GroupWorkspaceProjectPageMember aMember : members)
        	{
        	    if( aMember.getOrder() > order )
        	    {
        	    	aMember.setOrder(aMember.getOrder() - 1 );
        	    }
        	}
        }
        return removed;
    }
    
    /**
     * Move a group workspace project page member up.
     * 
     * @param member
     */
    public void moveMemberUp(GroupWorkspaceProjectPageMember member)
    {
    	// member is in the list
    	// number of members is greater than one
    	// and is not the first one in the list already
    	if(members.contains(member) && (members.size() > 1) && (member.getOrder() != 1 ))
    	{
    		for(GroupWorkspaceProjectPageMember aMember : members)
    		{
    			if(aMember.getOrder() == (member.getOrder() - 1) )
    			{
    				aMember.setOrder(aMember.getOrder() + 1);
    			}
    		}
    		member.setOrder(member.getOrder() - 1);
    	}
    	
    }
    
    /**
     * Move a group workspace project page member up - sets the one above down in the list
     * 
     * @param member
     */
    public void moveMemberDown(GroupWorkspaceProjectPageMember member)
    {
    	// member is in the list
    	// number of members is greater than one
    	// and is not the last one in the list already
    	if(members.contains(member) && (members.size() > 1) && (member.getOrder() < members.size() ) )
    	{
    		for(GroupWorkspaceProjectPageMember aMember : members)
    		{
    			if(aMember.getOrder() == (member.getOrder() + 1) )
    			{
    				aMember.setOrder(aMember.getOrder() - 1);
    			}
    		}
    		member.setOrder(member.getOrder() + 1);
    	}
    }
    
    /**
     * Get the group workspace project page image by file id.
     * 
     * @param irFileId - id of the file
     * @return Group Workspace project apge image
     */
    public GroupWorkspaceProjectPageImage getImageByFileId(Long irFileId)
    {
    	for(GroupWorkspaceProjectPageImage image : images)
    	{
    		if( image.getImageFile().getId().equals(irFileId))
    		{
    			return image;
    		}
    	}
    	return null;
    }
    
    
    /**
     * Move a group workspace project page image up.
     * 
     * @param member
     */
    public void moveImageUp(GroupWorkspaceProjectPageImage image)
    {
    	// image is in the list
    	// number of images is greater than one
    	// and is not the first one in the list already
    	if(images.contains(image) && (images.size() > 1) && (image.getOrder() != 1 ))
    	{
    		for(GroupWorkspaceProjectPageImage anImage : images)
    		{
    			if(anImage.getOrder() == (image.getOrder() - 1) )
    			{
    				anImage.setOrder(anImage.getOrder() + 1);
    			}
    		}
    		image.setOrder(image.getOrder() - 1);
    	}
    	
    }
    
    /**
     * Move a group workspace project page image down - sets the one above down in the list
     * 
     * @param member
     */
    public void moveImageDown(GroupWorkspaceProjectPageImage image)
    {
    	// image is in the list
    	// number of images is greater than one
    	// and is not the last one in the list already
    	if(images.contains(image) && (images.size() > 1) && (image.getOrder() < images.size() ) )
    	{
    		for(GroupWorkspaceProjectPageImage anImage : images)
    		{
    			if(anImage.getOrder() == (image.getOrder() + 1) )
    			{
    				anImage.setOrder(anImage.getOrder() - 1);
    			}
    		}
    		image.setOrder(image.getOrder() + 1);
    	}
    }
    
    
	/**
	 * Get the description.
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Get the group workspace.
	 * 
	 * @return
	 */
	public GroupWorkspace getGroupWorkspace() {
		return groupWorkspace;
	}

	/**
	 * Set the group workspace this project page is for.
	 * 
	 * @param groupWorkspace
	 */
	void setGroupWorkspace(GroupWorkspace groupWorkspace) {
		this.groupWorkspace = groupWorkspace;
	}
	
    /**
     * Get if this page is public.
     * 
     * @return true if the page is public
     */
    public boolean getPagePublic() {
		return pagePublic;
	}

	/**
	 * If Set to true if the page is public.
	 * 
	 * @param pagePublic
	 */
	public void setPagePublic(boolean pagePublic) {
		this.pagePublic = pagePublic;
	}
	
	/**
	 * Get the pictures 
	 * 
	 * @return
	 */
	public Set<GroupWorkspaceProjectPageImage> getImages() {
		return Collections.unmodifiableSet(images);
	}

	/**
	 * Set the pictures for the group project page.
	 * 
	 * @param pictures
	 */
	void setImages(Set<GroupWorkspaceProjectPageImage> pictures) {
		this.images = pictures;
	}
	
	/**
	 * Hash code is based on the name of
	 * the group space
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value +=  getGroupWorkspace().getLowerCaseName() == null ? 0 : getGroupWorkspace().getLowerCaseName().hashCode();
		return value;
	}
	
	/**
	 * Equals is tested based on name ignoring case 
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof GroupWorkspaceProjectPage)) return false;

		final GroupWorkspaceProjectPage other = (GroupWorkspaceProjectPage) o;

		if( ( getGroupWorkspace().getLowerCaseName() != null && !getGroupWorkspace().getLowerCaseName().equalsIgnoreCase(other.getGroupWorkspace().getLowerCaseName()) ) ||
			( getGroupWorkspace().getLowerCaseName() == null && other.getGroupWorkspace().getLowerCaseName() != null ) ) return false;

		return true;
	}
	
	/**
	 * Get the date this project page was created.
	 * 
	 * @return
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	/**
	 * Set the date this project page was created.
	 * 
	 * @param createdDate
	 */
	void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	
	/**
	 * Get the members for the group workspace. This is an 
	 * unmodifiable list.
	 * 
	 * @return
	 */
	public Set<GroupWorkspaceProjectPageMember> getMembers() {
		return Collections.unmodifiableSet(members);
	}
	
	/**
	 * Get the members by order.
	 * 
	 * @return
	 */
	public List<GroupWorkspaceProjectPageMember> getMembersByOrder()
	{
		List<GroupWorkspaceProjectPageMember> theMembers = new LinkedList<GroupWorkspaceProjectPageMember>();
	    theMembers.addAll(members);
	    Collections.sort(theMembers, new AscendingOrderComparator());
	    return Collections.unmodifiableList(theMembers);
	}
	
	/**
	 * Get the images by order.
	 * 
	 * @return
	 */
	public List<GroupWorkspaceProjectPageImage> getImagesByOrder()
	{
		List<GroupWorkspaceProjectPageImage> theImages = new LinkedList<GroupWorkspaceProjectPageImage>();
	    theImages.addAll(images);
	    Collections.sort(theImages, new AscendingOrderComparator());
	    return Collections.unmodifiableList(theImages);
	}

	/**
	 * Set the members in ghe group workspace project page.
	 * 
	 * @param members
	 */
	void setMembers(Set<GroupWorkspaceProjectPageMember> members) {
		this.members = members;
	}


}
