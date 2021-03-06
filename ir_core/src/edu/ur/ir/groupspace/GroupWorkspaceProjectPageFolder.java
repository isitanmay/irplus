/**  
   Copyright 2008 - 2012 University of Rochester

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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemType;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.item.GenericItem;
import edu.ur.persistent.LongPersistentId;
import edu.ur.persistent.PersistentVersioned;
import edu.ur.simple.type.DescriptionAware;
import edu.ur.simple.type.NameAware;
import edu.ur.tree.PreOrderTreeSetNodeBase;

/**
 * Group workspace project page folder - a folder to hold information for the group workspace
 * project page.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceProjectPageFolder extends PreOrderTreeSetNodeBase implements
Serializable,  LongPersistentId, PersistentVersioned,
DescriptionAware, NameAware, Comparable<GroupWorkspaceProjectPageFolder>, FileSystem{
	
	//eclipse generated id
	private static final long serialVersionUID = 7469105247544196829L;

	/** Logger */
	private static final Logger log = Logger.getLogger(GroupWorkspaceProjectPageFolder.class);
	
	/**  The id of the folder  */
	private Long id;
	
	/**  Name of the groupWorkspaceProjectPage folder */
	private String name;
	
	/** Description of the folder */
	private String description;
	
	/**  Version of the data read from the database.  */
	private int version;
	
	/** Root of the entire folder tree. */
	private GroupWorkspaceProjectPageFolder treeRoot;
	
	/** GroupWorkspaceProjectPageFolder the folder belongs to. */
	private GroupWorkspaceProjectPage groupWorkspaceProjectPage;
	
	/**  The type of object this represents */
	private FileSystemType fileSystemType = FileSystemType.GROUP_WORKSPACE_PROJECT_PAGE_FOLDER;

	/** Folders of the groupWorkspaceProjectPage */
	private Set<GroupWorkspaceProjectPageFolder> children = new HashSet<GroupWorkspaceProjectPageFolder>();
	
	/** Files of the groupWorkspaceProjectPage */
	private Set<GroupWorkspaceProjectPageFile> files = new HashSet<GroupWorkspaceProjectPageFile>();
	
	/** Publications of the groupWorkspaceProjectPage */
	private Set<GroupWorkspaceProjectPagePublication> publications = new HashSet<GroupWorkspaceProjectPagePublication>();

	/** Institutional items of the groupWorkspaceProjectPage */
	private Set<GroupWorkspaceProjectPageInstitutionalItem> institutionalItems = new HashSet<GroupWorkspaceProjectPageInstitutionalItem>();
	
	/** Links of the groupWorkspaceProjectPage */
	private Set<GroupWorkspaceProjectPageFileSystemLink> links = new HashSet<GroupWorkspaceProjectPageFileSystemLink>();
		
	/**
	 * This is the conceptual path to the folder.
	 * The base path plus the root of the tree 
	 * down to itself.
	 * For example if you have a parent A
	 * the following sub folders  
	 *  B, C and D and A is a parent of B and 
	 * B is a parent of C and C is a parent of D Then the
	 * paths are as follows:
	 * 
	 *  /A/
	 *  /A/B/
	 *  /A/B/C/
	 *  /A/B/C/D/
	 * 
	 */
	private String path;
	
	/**
	 * Default Constructor 
	 */
	GroupWorkspaceProjectPageFolder()
	{
		this.treeRoot = this;
	}
	
	/**
	 * Default public constructor.  A valid groupWorkspaceProjectPage
	 * and folder name must be passed.
	 * 
	 * @param groupWorkspaceProjectPage - groupWorkspaceProjectPage who will own the folder
	 * @param folderName - name of the folder
	 * 
	 * @throws IllegalStateException if the folder name or
	 * groupWorkspaceProjectPage are null.
	 */
	GroupWorkspaceProjectPageFolder(GroupWorkspaceProjectPage groupWorkspaceProjectPage, String folderName)
	{
		if(folderName == null)
		{
			throw new IllegalStateException("folder name cannot be null");
		}
		
		if( groupWorkspaceProjectPage == null )
		{
			throw new IllegalStateException("GroupWorkspaceProjectPageFolder cannot be null");
		}
		
		setTreeRoot(this);
		setGroupWorkspaceProjectPage(groupWorkspaceProjectPage);
		setName(folderName);
		setPath(PATH_SEPERATOR);
	}
	
	/**
	 * Find a GroupWorkspaceProjectPageFolder based on the name.  If
	 * no GroupWorkspaceProjectPageFolder is found a null object is returned.
	 * 
	 * This is <b>NOT</b>  a recursive operation and only searches
	 * the current list of children.
	 * 
	 * @param name of the child groupWorkspaceProjectPage folder.
	 * @return the found groupWorkspaceProjectPage folder.
	 */
	public GroupWorkspaceProjectPageFolder getChild(String name)
	{
		if( log.isDebugEnabled() )
		{
			log.debug("Searching for groupWorkspaceProjectPage folder " + name.trim());
		}
		GroupWorkspaceProjectPageFolder GroupWorkspaceProjectPageFolder = null;
		boolean found = false;
		Iterator<GroupWorkspaceProjectPageFolder> iter = children.iterator();
		
		while( iter.hasNext() && !found)
		{
			GroupWorkspaceProjectPageFolder c = iter.next();
			if( c.getName().equalsIgnoreCase(name.trim()))
			{
				GroupWorkspaceProjectPageFolder = c;
				found = true;
		    }
		}
	
		return GroupWorkspaceProjectPageFolder;
	}	

	/**
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getChildren()
	 */
	@Override
	public Set<GroupWorkspaceProjectPageFolder> getChildren() {
		return Collections.unmodifiableSet(new TreeSet<GroupWorkspaceProjectPageFolder>(children));
	}

	/**
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getParent()
	 */
	@Override
	public GroupWorkspaceProjectPageFolder getParent() {
		return (GroupWorkspaceProjectPageFolder)parent;
	}
	
	/**
	 * @see edu.ur.persistent.LongPersistentId#getId()
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @see edu.ur.persistent.PersistentVersioned#getVersion()
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Get the description of this collection
	 * 
	 * @see edu.ur.common.DescriptionAware#getDescription()
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Description of the collection.
	 * 
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * Get the name of this collection
	 * 
	 * @see edu.ur.common.NameAware#getName()
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Name of the folder.  Will trim the name if it
	 * has extra spaces on the left or right
	 * 
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name.trim();
	}
	
	/**
	 * Add a file to this folder.  this creates a 
	 * new groupWorkspaceProjectPage file record.
	 * 
	 * @param irFile
	 */
	public GroupWorkspaceProjectPageFile addFile(IrFile irFile) {
		GroupWorkspaceProjectPageFile rf = getFile(irFile);
		if( rf == null )
		{
		    rf = new GroupWorkspaceProjectPageFile(groupWorkspaceProjectPage, irFile, this);
		    add(rf);
		}
		return rf;
	}

	/**
	 * Add a file to this folder.  this creates a 
	 * new groupWorkspaceProjectPage file record.
	 * 
	 * @param irFile
	 */
	public GroupWorkspaceProjectPagePublication createPublication(GenericItem item, int versionNumber) {
		GroupWorkspaceProjectPagePublication rp = getPublication(item);
		if( rp == null )
		{
		    rp = new GroupWorkspaceProjectPagePublication(groupWorkspaceProjectPage, this, item, versionNumber);
		    publications.add(rp);
		}
		return rp;
	}

	/**
	 * Add a institutional Item to this folder.  this creates a 
	 * new groupWorkspaceProjectPage institutional Item record.
	 * 
	 * @param institutionalItem
	 */
	public GroupWorkspaceProjectPageInstitutionalItem create(InstitutionalItem institutionalItem) {
		GroupWorkspaceProjectPageInstitutionalItem ri = getInstitutionalItem(institutionalItem);
		if( ri == null )
		{
		    ri = new GroupWorkspaceProjectPageInstitutionalItem(groupWorkspaceProjectPage, this, institutionalItem);
		    institutionalItems.add(ri);
		}
		return ri;
	}
	
	/**
	 * Get the groupWorkspaceProjectPage institutional item based on the institutional item
	 * 
	 * @param institutionalItem - institutional item to check for
	 * @return - the groupWorkspaceProjectPage institutional item or null if not found
	 */
	public GroupWorkspaceProjectPageInstitutionalItem getInstitutionalItem(InstitutionalItem institutionalItem)
	{
		for( GroupWorkspaceProjectPageInstitutionalItem item : institutionalItems)
		{
			if(item.getInstitutionalItem().equals(institutionalItem))
			{
				return item;
			}
		}
		return null;
	}
	
	/**
	 * Get a publication based on the generic item.
	 * 
	 * @param publication - publication in the groupWorkspaceProjectPage publication
	 * @return the found groupWorkspaceProjectPage publication or null
	 */
	public GroupWorkspaceProjectPagePublication getPublication(GenericItem publication)
	{
		for(GroupWorkspaceProjectPagePublication pub :publications)
		{
			if( pub.getPublication().equals(publication))
			{
				return pub;
			}
		}
		return null;
	}
	
	/**
	 * Get the groupWorkspaceProjectPage file based on the ir file.
	 * 
	 * @param f - IR file the groupWorkspaceProjectPage file should contain.
	 * @return - the found groupWorkspaceProjectPage file
	 */
	public GroupWorkspaceProjectPageFile getFile(IrFile f)
	{
		for(GroupWorkspaceProjectPageFile file : files)
		{
			if( file.getIrFile().equals(f ))
			{
				return file;
			}
		}
		
		return null;
	}
	
	/**
	 * Get the groupWorkspaceProjectPage file based on the ir file.
	 * 
	 * @param fileId - id of the ir file.
	 * @return - the found groupWorkspaceProjectPage file otherwise nul
	 */
	public GroupWorkspaceProjectPageFile getFileByIrFileId(Long irFileId)
	{
		for(GroupWorkspaceProjectPageFile file : files)
		{
			if( file.getIrFile().getId().equals(irFileId))
			{
				return file;
			}
		}
		
		return null;
	}
	
	/**
	 * Add a file to this folder.  this creates a 
	 * new groupWorkspaceProjectPage file record.
	 * 
	 * @param irFile
	 */
	public GroupWorkspaceProjectPageFileSystemLink createLink(String name, String url, String description) throws DuplicateNameException
	{
		 GroupWorkspaceProjectPageFileSystemLink rl = new GroupWorkspaceProjectPageFileSystemLink(groupWorkspaceProjectPage, this, url);
		 rl.setName(name);
		 rl.setDescription(description);
		 
		 if( links.contains( rl))
		 {
			 throw new DuplicateNameException("Link " + rl + " already exists ", name);
		 }
		 links.add(rl);
		 return rl;
	}

	
	/**
	 * Remove a file from the groupWorkspaceProjectPage folder
	 * 
	 * @param rf - file to be removed
	 * @return true if the file is removed.
	 */
	public boolean remove(GroupWorkspaceProjectPageFile rf)
	{
		boolean removed = false;
		if( files.contains(rf))
		{
			removed = files.remove(rf);
			rf.setParentFolder(null);
			rf.setParentFolder(null);
		}
		return removed;
	}
	
	/**
	 * Remove a publication from the groupWorkspaceProjectPage folder
	 * 
	 * @param rp
	 * @return true if the publication is removed.
	 */
	public boolean remove(GroupWorkspaceProjectPagePublication rp)
	{
		boolean removed = false;
		if( publications.contains(rp))
		{
			removed = publications.remove(rp);
			rp.setParentFolder(null);
		}
		return removed;
	}

	/**
	 * Remove a institutional Item from the groupWorkspaceProjectPage folder
	 * 
	 * @param rp
	 * @return true if the institutional Item is removed.
	 */
	public boolean remove(GroupWorkspaceProjectPageInstitutionalItem ri)
	{
		boolean removed = false;
		if( institutionalItems.contains(ri))
		{
			removed = institutionalItems.remove(ri);
			ri.setParentFolder(null);
		}
		return removed;
	}
	
	/**
	 * Remove a link from the groupWorkspaceProjectPage folder
	 * 
	 * @param rl
	 * @return true if the link is removed.
	 */
	public boolean remove(GroupWorkspaceProjectPageFileSystemLink rl)
	{
		boolean removed = false;
		if( links.contains(rl))
		{
			removed = links.remove(rl);
			rl.setParentFolder(null);
		}
		return removed;
	}
	
	/**
	 * Find a file based on the name.  If
	 * no file is found a null object is returned.  This
	 * is case in-sensitive
	 * 
	 * @param name of the file including the extension
	 * @return the found file
	 */
	public GroupWorkspaceProjectPageFile getGroupWorkspaceProjectPageFile(String name)
	{
		for(GroupWorkspaceProjectPageFile rf : files)
		{
			if( rf.getName().equalsIgnoreCase(name))
			{
				return rf;
			}
		}
		
		return null;
	}

	/**
	 * Find a file based on the name.  If
	 * no file is found a null object is returned.  This
	 * is case in-sensitive
	 * 
	 * @param name of the file including the extension
	 * @return the found file
	 */
	public GroupWorkspaceProjectPageFileSystemLink getLink(String name)
	{
		for(GroupWorkspaceProjectPageFileSystemLink rl : links)
		{
			if( rl.getName().equalsIgnoreCase(name))
			{
				return rl;
			}
		}
		
		return null;
	}
	
	/**
	 * Find a file based on the irFile.  If
	 * no file is found a null object is returned.  
	 * 
	 * @param irFile - the file
	 * @return the found file
	 */
	public GroupWorkspaceProjectPageFile getGroupWorkspaceProjectPageFile(IrFile irFile)
	{
		for(GroupWorkspaceProjectPageFile rf : files)
		{
			if( rf.getIrFile().equals(irFile))
			{
				return rf;
			}
		}
		
		return null;
	}

	/**
	 * Find a groupWorkspaceProjectPage Institutional Item based on the Institutional Item.  If
	 * no Institutional Item is found a null object is returned.  
	 * 
	 * @param item - the Institutional Item
	 * @return the found Institutional Item
	 */
	public GroupWorkspaceProjectPageInstitutionalItem getGroupWorkspaceProjectPageInstitutionalItem(InstitutionalItem item)
	{
		for(GroupWorkspaceProjectPageInstitutionalItem ri : institutionalItems)
		{
			if( ri.getInstitutionalItem().equals(item))
			{
				return ri;
			}
		}
		
		return null;
	}

	/**
	 * An unmodifiable set of files in this groupWorkspaceProjectPage folder
	 * 
	 * 
	 * @return the set of items.
	 */
	public Set<GroupWorkspaceProjectPageFile> getFiles() {
		return Collections.unmodifiableSet(files);
	}

	/**
	 * Files in this folder
	 * 
	 * @param items
	 */
	void setFiles(Set<GroupWorkspaceProjectPageFile> files) {
		this.files = files;
	}
	
	/**
	 * Returns a folder root.
	 * 
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getRoot()
	 */
	@Override
	public GroupWorkspaceProjectPageFolder getTreeRoot() {
		return treeRoot;
	}

	/**
	 * Remove the child from the set of children. 
	 * Renumbers the tree to be correct.
	 * 
	 * @param child
	 * @return true if the child is removed.
	 */
	public boolean removeChild(GroupWorkspaceProjectPageFolder child)
	{
		boolean removed = false;
		
		if( children.remove(child) )
		{
			log.debug("removing child " + child);
			removed = true;
			
			// re-number the tree
			cleanUpTree(child.getTreeSize(), child.getRightValue());
			log.debug( "child removed tree values = " + this);
			child.setParent(null);
	    }
		return removed;
	}
	
	/**
	 * Add a child folder to this groupWorkspaceProjectPage folder's set of children.
	 * 
	 * @param child to add
	 */
	public void addChild(GroupWorkspaceProjectPageFolder child) throws DuplicateNameException
	{

		// return if this already has the child
		if( children.contains(child))
		{
			return;
		}

		if( child.equals(this))
		{
			throw new IllegalStateException("cannot add a groupWorkspaceProjectPage folder to " +
					"itself as a child");
		}
		if(!isVaildGroupWorkspaceProjectPageFileSystemName(child.getName()))
		{
			throw new DuplicateNameException("Folder with the name " + 
					child.getName() + " already exists ", child.getName());
		}
		if (!child.isRoot()) {
			GroupWorkspaceProjectPageFolder childParent = child.getParent();
			childParent.removeChild(child);
		}

		child.setTreeRoot(null);
		makeRoomInTree(child);
		child.setParent(this);
		child.setGroupWorkspaceProjectPage(groupWorkspaceProjectPage);
		child.setTreeRoot(getTreeRoot());
		children.add(child);
	}
	
	/**
	 * Create a new child folder for this 
	 * groupWorkspaceProjectPage folder.
	 * 
	 * @param name of the child folder.
	 * @return the created folder
	 * 
	 * @throws IllegalArgumentException if a name of a folder that
	 * already exists is passed in.
	 */
	public GroupWorkspaceProjectPageFolder createChild(String name) throws DuplicateNameException
	{
	    GroupWorkspaceProjectPageFolder c = new GroupWorkspaceProjectPageFolder();
		c.setName(name);
		addChild(c);
		return c;
	}
	
	/**
	 * Returns true if the name is ok to add to a file or folder
	 * 
	 * @param name of the file or folder.  If it is a file, it should contain
	 * the extension.
	 * 
	 * @return true if the name does not exist.  This is case insensitive.
	 */
	private boolean isVaildGroupWorkspaceProjectPageFileSystemName(String name)
	{
		boolean ok = false;
		if( getChild(name) == null && getGroupWorkspaceProjectPageFile(name) == null)
		{
			ok = true;
		}
		return ok;
	}

	/**
	 * Set the children in this folder.
	 * 
	 * @param children
	 */
	void setChildren(Set<GroupWorkspaceProjectPageFolder> children) {
		this.children = children;
	}

	/**
	 * Set the id of this folder.
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Set the parent of this groupWorkspaceProjectPage folder.  Results
	 * in setting path and all children paths.
	 * 
	 * @param parent
	 */
	void setParent(GroupWorkspaceProjectPageFolder parent) {
		if(parent == null)
		{
			path = GroupWorkspaceProjectPageFolder.PATH_SEPERATOR ;
		}		
		super.setParent(parent);
	}

	/**
	 * Set the root folder for the entire tree.  
	 * This recursively updates the children.
	 * 
	 * @param root
	 */
	void setTreeRoot(GroupWorkspaceProjectPageFolder root) {
		this.treeRoot = root;
		for(GroupWorkspaceProjectPageFolder c: children)
		{
			c.setTreeRoot(root);
		}
	}

	/**
	 * Set the version of data for the database data
	 * of this folder.
	 * 
	 * @param version
	 */
	public void setVersion(int version) {
		this.version = version;
	}
		
	/**
	 * Get the path.  This should be
	 * the groupWorkspaceProjectPage name plus the path of all 
	 * parents.
	 * 
	 * @throws IllegalStateException if the folder is the root 
	 * folder and the groupWorkspaceProjectPage does not exist.
	 *  
	 * @return the path
	 */
	public String getPath() 
	{
		return path;
	}
	
	/**
	 * Get the full path of this folder.
	 * 
	 * @return the full path
	 */
	public String getFullPath()
	{
		return getPath() + name + PATH_SEPERATOR;
	}

	/**
	 * Set the conceptual path for this folder.
	 * This DOES NOT update children paths.
	 * 
	 * @see updatePaths
	 * 
	 * @param path
	 */
	void setPath(String path) {
		
		path = path.trim();
		
        // verify the path is correct
		verifyPath(path);

		// add the end seperator if it does not exist
		if (!path.substring(path.length()-1, path.length()).equals(PATH_SEPERATOR)) {
			path = path + PATH_SEPERATOR;
		}
		
		this.path = path;
	}
	
	/**
	 * Make sure the path is correct. A root folder always returns true.
	 * 
	 * @param path -
	 *            the path of this object
	 * @return - true if the parent's full path matches this folders path or
	 *         this is the root folder.
	 * 
	 * 
	 */
	private boolean verifyPath(String path) {
		boolean valid = true;
		
        //make sure there is a beginning separator
		if(!path.substring(0,1).equals(PATH_SEPERATOR))
		{
		    throw new IllegalStateException("No beginning path Seperator path = " + path);
		}
		else if (!isRoot()) {
			if (!(path.equals(parent.getFullPath()))) {
				throw new IllegalArgumentException("Path is : " + path
						+ " but should equal parent path "
						+ " plus parents name which = " + parent.getFullPath());
			}
		}
		return valid;
	}
	
	/**
	 * Hash code is based on the path and name of
	 * the folder.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += groupWorkspaceProjectPage == null ? 0 : groupWorkspaceProjectPage.hashCode();
		value += getFullPath() == null? 0 : getFullPath().hashCode();
		return value;
	}
	
	/**
	 * Equals is tested based on name and path of the
	 * object.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof GroupWorkspaceProjectPageFolder)) return false;

		final GroupWorkspaceProjectPageFolder other = (GroupWorkspaceProjectPageFolder) o;

		if( ( groupWorkspaceProjectPage != null && !groupWorkspaceProjectPage.equals(other.getGroupWorkspaceProjectPage()) ) ||
			( groupWorkspaceProjectPage == null && other.getGroupWorkspaceProjectPage() != null ) ) return false;
		
		if( ( getFullPath() != null && !getFullPath().equals(other.getFullPath()) ) ||
		    ( getFullPath() == null && other.getFullPath() != null ) ) return false;

		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[GroupWorkspaceProjectPageFolder id = ");
		sb.append(id);
		sb.append(" name = ");
		sb.append(name);
		sb.append(" path = ");
		sb.append(path);
		sb.append( " left value = ");
		sb.append(leftValue);
		sb.append(" right value = ");
		sb.append(rightValue);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Sets the path and the path of all
	 * children. This should be used when a
	 * path should be passed down to all children.
	 * 
	 * @param path
	 */
	protected void updatePaths(String path)
	{
		setPath(path);
		for(GroupWorkspaceProjectPageFolder c: children)
		{
			c.updatePaths(getFullPath());
		}
	}
	 
	/**
	 * Compares two groupWorkspaceProjectPage folders by name.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(GroupWorkspaceProjectPageFolder other) {
		return this.getName().compareTo(other.getName());
	}


	/* (non-Javadoc)
	 * @see edu.ur.ir.FileSystem#getType()
	 */
	public FileSystemType getFileSystemType() {
		return fileSystemType;
	}

	/**
	 * Always returns -1
	 * 
	 * @see edu.ur.ir.FileSystem#getVersionNumber()
	 */
	public int getVersionNumber() {
		return -1;
	}

	/**
	 * This is not a versioned entity
	 * @see edu.ur.ir.FileSystem#getVersioned()
	 */
	public boolean getVersioned() {
		return false;
	}

	@Override
	protected void setRoot(PreOrderTreeSetNodeBase root) {
		this.treeRoot = (GroupWorkspaceProjectPageFolder)root;
	}

	/**
	 * Get the groupWorkspaceProjectPage who owns the folder.
	 * 
	 * @return
	 */
	public GroupWorkspaceProjectPage getGroupWorkspaceProjectPage() {
		return groupWorkspaceProjectPage;
	}

	/**
	 * Set the groupWorkspaceProjectPage who owns the folder.
	 * 
	 * @param groupWorkspaceProjectPage
	 */	
	void setGroupWorkspaceProjectPage(GroupWorkspaceProjectPage groupWorkspaceProjectPage) {
		this.groupWorkspaceProjectPage = groupWorkspaceProjectPage;
	}

	/**
	 * Get the set of publications for the groupWorkspaceProjectPage - this
	 * is an unmodifiable set.
	 * 
	 * @return
	 */
	public Set<GroupWorkspaceProjectPagePublication> getPublications() {
		return Collections.unmodifiableSet(publications);
	}

	/**
	 * Set the publications for the groupWorkspaceProjectPage folder.
	 * 
	 * @param publications
	 */
	void setPublications(Set<GroupWorkspaceProjectPagePublication> publications) {
		this.publications = publications;
	}

	/**
	 * Get the groupWorkspaceProjectPage links in this folder - this is an unmodifiable set.
	 * 
	 * @return set of reseracher links
	 */
	public Set<GroupWorkspaceProjectPageFileSystemLink> getLinks() {
		return Collections.unmodifiableSet(links);
	}

	/**
	 * Set the links for the groupWorkspaceProjectPage folder.
	 * 
	 * @param links
	 */
	void setLinks(Set<GroupWorkspaceProjectPageFileSystemLink> links) {
		this.links = links;
	}
	
	/**
	 * Adds publication to a folder
	 * 
	 * @param rp publication to add groupWorkspaceProjectPage folder
	 * 
	 */
	public void addPublication(GroupWorkspaceProjectPagePublication rp) {
		if (!publications.contains(rp)) {
			if( rp.getParentFolder() != null )
			{
				rp.getParentFolder().remove(rp);
			}
			
			rp.setParentFolder(this);
			publications.add(rp);
		}
	}
	
	/**
	 * Adds an existing researcher file to this folder.  This allows
	 * a researcher file to be moved from one location to another.
	 * 
	 * @param rf - researcher file to add
	 * @throws DuplicateNameException - if the file already exits
	 */
	public void add(GroupWorkspaceProjectPageFile file) 
	{
		if(!files.contains(file))
		{
			if( file.getParentFolder() != null )
			{
				file.getParentFolder().remove(file);
			}
			
			file.setParentFolder(this);
			files.add(file);
		}
	}

	/**
	 * Adds Institutional Item to a folder
	 * 
	 * @param ri Institutional Item to add groupWorkspaceProjectPage folder
	 * 
	 */
	public void addInstitutionalItem(GroupWorkspaceProjectPageInstitutionalItem ri) {
		if (!institutionalItems.contains(ri)) {
			if( ri.getParentFolder() != null )
			{
				ri.getParentFolder().remove(ri);
			}
			
			ri.setParentFolder(this);
			institutionalItems.add(ri);
		}
	}
	
	/**
	 * Adds link to a folder
	 * 
	 * @param rl link to add groupWorkspaceProjectPage folder
	 * 
	 */
	public void addLink(GroupWorkspaceProjectPageFileSystemLink rl) {
		if (!links.contains(rl)) {
			if( rl.getParentFolder() != null )
			{
				rl.getParentFolder().remove(rl);
			}
			rl.setParentFolder(this);
			links.add(rl);
		}
	}
	
	/**
	 * Returns an unmodifiable set of institutional items.
	 * 
	 * @return set of institutional items for this groupWorkspaceProjectPage.
	 */
	public Set<GroupWorkspaceProjectPageInstitutionalItem> getInstitutionalItems() {
		return Collections.unmodifiableSet(institutionalItems);
	}

	/**
	 * Set the set of institutional items.
	 * 
	 * @param institutionalItems
	 */
	void setInstitutionalItems(
			Set<GroupWorkspaceProjectPageInstitutionalItem> institutionalItems) {
		this.institutionalItems = institutionalItems;
	}

}
