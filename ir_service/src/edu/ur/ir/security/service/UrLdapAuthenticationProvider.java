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


package edu.ur.ir.security.service;



import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import edu.ur.ir.user.ExternalAccountType;
import edu.ur.ir.user.ExternalAccountTypeService;


/**
 * NOTE: Based on LdapAuthenticationProvider originally written by Luke Taylor.
 * 
 * This takes an LDAP token and prevents the DAO and LDAP authentcations from both being tried for a login 
 * 
 * An {@link org.springframework.security.providers.AuthenticationProvider} implementation that authenticates
 * against an LDAP server.
 * <p>
 * There are many ways in which an LDAP directory can be configured so this class delegates most of
 * its responsibilites to two separate strategy interfaces, {@link LdapAuthenticator}
 * and {@link LdapAuthoritiesPopulator}.
 *
 * <h3>LdapAuthenticator</h3>
 * This interface is responsible for performing the user authentication and retrieving
 * the user's information from the directory. Example implementations are {@link
 * org.springframework.security.providers.ldap.authenticator.BindAuthenticator BindAuthenticator} which authenticates
 * the user by "binding" as that user, and
 * {@link org.springframework.security.providers.ldap.authenticator.PasswordComparisonAuthenticator PasswordComparisonAuthenticator}
 * which compares the supplied password with the value stored in the directory, using an LDAP "compare"
 * operation.
 * <p>
 * The task of retrieving the user attributes is delegated to the authenticator because the permissions on the
 * attributes may depend on the type of authentication being used; for example, if binding as the user, it may be
 * necessary to read them with the user's own permissions (using the same context used for the bind operation).
 *
 * <h3>LdapAuthoritiesPopulator</h3>
 * Once the user has been authenticated, this interface is called to obtain the set of granted authorities for the
 * user.
 * The {@link DefaultLdapAuthoritiesPopulator DefaultLdapAuthoritiesPopulator}
 * can be configured to obtain user role information from the user's attributes and/or to perform a search for
 * "groups" that the user is a member of and map these to roles.
 *
 * <p>
 * A custom implementation could obtain the roles from a completely different source, for example from a database.
 *
 * <h3>Configuration</h3>
 *
 * A simple configuration might be as follows:
 * <pre>
 *   &lt;bean id=&quot;contextSource&quot;
 *       class=&quot;org.springframework.security.ldap.DefaultSpringSecurityContextSource&quot;&gt;
 *     &lt;constructor-arg value=&quot;ldap://monkeymachine:389/dc=springframework,dc=org&quot;/&gt;
 *     &lt;property name=&quot;userDn&quot; value=&quot;cn=manager,dc=springframework,dc=org&quot;/&gt;
 *     &lt;property name=&quot;password&quot; value=&quot;password&quot;/&gt;
 *   &lt;/bean&gt;
 *
 *   &lt;bean id=&quot;ldapAuthProvider&quot;
 *       class=&quot;org.springframework.security.providers.ldap.LdapAuthenticationProvider&quot;&gt;
 *     &lt;constructor-arg&gt;
 *       &lt;bean class=&quot;org.springframework.security.providers.ldap.authenticator.BindAuthenticator&quot;&gt;
 *           &lt;constructor-arg ref=&quot;contextSource&quot;/&gt;
 *           &lt;property name=&quot;userDnPatterns&quot;&gt;&lt;list&gt;&lt;value&gt;uid={0},ou=people&lt;/value&gt;&lt;/list&gt;&lt;/property&gt;
 *       &lt;/bean&gt;
 *     &lt;/constructor-arg&gt;
 *     &lt;constructor-arg&gt;
 *       &lt;bean class=&quot;org.springframework.security.ldap.populator.DefaultLdapAuthoritiesPopulator&quot;&gt;
 *           &lt;constructor-arg ref=&quot;contextSource&quot;/&gt;
 *           &lt;constructor-arg value=&quot;ou=groups&quot;/&gt;
 *           &lt;property name=&quot;groupRoleAttribute&quot; value=&quot;ou&quot;/&gt;
 *       &lt;/bean&gt;
 *     &lt;/constructor-arg&gt;
 *   &lt;/bean&gt;
 *    </pre>
 *
 * <p>
 * This would set up the provider to access an LDAP server with URL
 * <tt>ldap://monkeymachine:389/dc=springframework,dc=org</tt>. Authentication will be performed by attempting to bind
 * with the DN <tt>uid=&lt;user-login-name&gt;,ou=people,dc=springframework,dc=org</tt>. After successful
 * authentication, roles will be assigned to the user by searching under the DN
 * <tt>ou=groups,dc=springframework,dc=org</tt> with the default filter <tt>(member=&lt;user's-DN&gt;)</tt>. The role
 * name will be taken from the "ou" attribute of each match.
 * <p>
 * The authenticate method will reject empty passwords outright. LDAP servers may allow an anonymous
 * bind operation with an empty password, even if a DN is supplied. In practice this means that if
 * the LDAP directory is configured to allow unauthenticated access, it might be possible to
 * authenticate as <i>any</i> user just by supplying an empty password.
 * More information on the misuse of unauthenticated access can be found in
 * <a href="http://www.ietf.org/internet-drafts/draft-ietf-ldapbis-authmeth-19.txt">
 * draft-ietf-ldapbis-authmeth-19.txt</a>.
 *
 *
 * @author Nathan Sarr
 * @version $Id$
 *
 * @see org.springframework.security.providers.ldap.authenticator.BindAuthenticator
 * @see DefaultLdapAuthoritiesPopulator
 */

public class UrLdapAuthenticationProvider implements AuthenticationProvider, Serializable {
    
	/** eclipse generated id  */
	private static final long serialVersionUID = -8847083271809838452L;


	//~ Static fields/initializers =====================================================================================
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultSecurityService.class);
	
	
    //~ Instance fields ================================================================================================

    protected transient MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private transient LdapAuthenticator authenticator;
    private transient LdapAuthoritiesPopulator authoritiesPopulator;
    private transient UserDetailsContextMapper userDetailsContextMapper = new LdapUserDetailsMapper();
    private boolean useAuthenticationRequestCredentials = true;
    
    /**  name of the of external authentication*/
    private String externalAccountTypeName;
    
    /** Service to deal with external account type information */
    private ExternalAccountTypeService externalAccountTypeService;

    //~ Constructors ===================================================================================================

    /**
     * Create an instance with the supplied authenticator and authorities populator implementations.
     *
     * @param authenticator the authentication strategy (bind, password comparison, etc)
     *          to be used by this provider for authenticating users.
     * @param authoritiesPopulator the strategy for obtaining the authorities for a given user after they've been
     *          authenticated.
     */
    public UrLdapAuthenticationProvider(LdapAuthenticator authenticator, LdapAuthoritiesPopulator authoritiesPopulator) {
        this.setAuthenticator(authenticator);
        this.setAuthoritiesPopulator(authoritiesPopulator);
    }

    /**
     * Creates an instance with the supplied authenticator and a null authorities populator.
     * In this case, the authorities must be mapped from the user context.
     *
     * @param authenticator the authenticator strategy.
     */
    public UrLdapAuthenticationProvider(LdapAuthenticator authenticator) {
        this.setAuthenticator(authenticator);
        this.setAuthoritiesPopulator(new NullAuthoritiesPopulator());
    }

    //~ Methods ========================================================================================================

    private void setAuthenticator(LdapAuthenticator authenticator) {
        Assert.notNull(authenticator, "An LdapAuthenticator must be supplied");
        this.authenticator = authenticator;
    }

    private LdapAuthenticator getAuthenticator() {
        return authenticator;
    }

    private void setAuthoritiesPopulator(LdapAuthoritiesPopulator authoritiesPopulator) {
        Assert.notNull(authoritiesPopulator, "An LdapAuthoritiesPopulator must be supplied");
        this.authoritiesPopulator = authoritiesPopulator;
    }

    protected LdapAuthoritiesPopulator getAuthoritiesPopulator() {
        return authoritiesPopulator;
    }

    public void setUserDetailsContextMapper(UserDetailsContextMapper userDetailsContextMapper) {
        Assert.notNull(userDetailsContextMapper, "UserDetailsContextMapper must not be null");
        this.userDetailsContextMapper = userDetailsContextMapper;
    }

    protected UserDetailsContextMapper getUserDetailsContextMapper() {
        return userDetailsContextMapper;
    }

    /**
     * Determines whether the supplied password will be used as the credentials in the successful authentication
     * token. If set to false, then the password will be obtained from the UserDetails object
     * created by the configured mapper. Often it will not be possible to read the password from the directory, so
     * defaults to true.
     *
     * @param useAuthenticationRequestCredentials
     */
    public void setUseAuthenticationRequestCredentials(boolean useAuthenticationRequestCredentials) {
        this.useAuthenticationRequestCredentials = useAuthenticationRequestCredentials;
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        
    	Assert.isInstanceOf(LdapAuthenticationToken.class, authentication,
            messages.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports",
                "Only UsernamePasswordAuthenticationToken is supported"));

    	LdapAuthenticationToken userToken = (LdapAuthenticationToken)authentication;

        String username = userToken.getName();
        
 
        if (!StringUtils.hasLength(username)) {
            throw new BadCredentialsException(messages.getMessage("LdapAuthenticationProvider.emptyUsername",
                    "Empty Username"));
        }

        String password = (String) authentication.getCredentials();
        Assert.notNull(password, "Null password was supplied in authentication token");

        if (password.length() == 0) {
            log.debug("Rejecting empty password for user " + username);
            throw new BadCredentialsException(messages.getMessage("LdapAuthenticationProvider.emptyPassword",
                    "Empty Password"));
        }

        try {
        	
        	// convert to username/password for bind
        	DirContextOperations userData = getAuthenticator().authenticate(new UsernamePasswordAuthenticationToken(username, password));
            Collection<GrantedAuthority> extraAuthorities = loadUserAuthorities(userData, username, password);
            UserDetails user = userDetailsContextMapper.mapUserFromContext(userData, username, extraAuthorities);

            if( user != null )
            {
                if (!user.isAccountNonLocked()) {
                    throw new LockedException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked",
                        "User account is locked"), user);
                }

                if (!user.isEnabled()) {
                    throw new DisabledException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled",
                        "User is disabled"), user);
                }

                if (!user.isAccountNonExpired()) {
                    throw new AccountExpiredException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.expired",
                        "User account has expired"), user);
                }
            }
            
            log.debug("creating successful authentication");
            return createSuccessfulAuthentication(userToken, user);

        } catch (NamingException ldapAccessFailure) {
            throw new AuthenticationServiceException(ldapAccessFailure.getMessage(), ldapAccessFailure);
        }
        catch(UsernameNotFoundException usernameNotFoundException)
        {
        	throw new BadCredentialsException(usernameNotFoundException.getMessage(), usernameNotFoundException);
        }
    }

    protected Collection<GrantedAuthority> loadUserAuthorities(DirContextOperations userData, String username, String password) {
        return getAuthoritiesPopulator().getGrantedAuthorities(userData, username);
    }

    protected Authentication createSuccessfulAuthentication(LdapAuthenticationToken authentication,
            UserDetails user) {
        Object password = useAuthenticationRequestCredentials ? authentication.getCredentials() : user.getPassword();

        LdapAuthenticationToken authenticationToken = null;
        ExternalAccountType externalAccountType = externalAccountTypeService.get(externalAccountTypeName);
        DefaultExternalAuthenticationDetails externalAuthenictaionDetails = new DefaultExternalAuthenticationDetails(externalAccountType);

        if( user != null )
        {
            authenticationToken =  new LdapAuthenticationToken(user, password, user.getAuthorities());
        }
        else
        {
        	// this means the user authenticated but they are not yet part of the irplus system
        	authenticationToken =  new LdapAuthenticationToken(user, password, null);
        }
        
        log.debug("adding details " + externalAuthenictaionDetails);
        authenticationToken.setDetails(externalAuthenictaionDetails);
        log.debug("added details " + authenticationToken.getDetails());
       
        return authenticationToken;
    }

    @SuppressWarnings("unchecked")
	public boolean supports(Class authentication) {
        return (LdapAuthenticationToken.class.isAssignableFrom(authentication));
    }

    //~ Inner Classes ==================================================================================================

    private static class NullAuthoritiesPopulator implements LdapAuthoritiesPopulator {
        public Collection<GrantedAuthority> getGrantedAuthorities(DirContextOperations userDetails, String username) {
            return new LinkedList<GrantedAuthority>();
        }
    }

	
	public ExternalAccountTypeService getExternalAccountTypeService() {
		return externalAccountTypeService;
	}

	public void setExternalAccountTypeService(
			ExternalAccountTypeService externalAccountTypeService) {
		this.externalAccountTypeService = externalAccountTypeService;
	}

	public String getExternalAccountTypeName() {
		return externalAccountTypeName;
	}

	public void setExternalAccountTypeName(String externalAccountTypeName) {
		this.externalAccountTypeName = externalAccountTypeName;
	}

}


