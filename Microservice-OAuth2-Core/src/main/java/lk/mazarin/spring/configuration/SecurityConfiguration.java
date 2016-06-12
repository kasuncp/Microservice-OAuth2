package lk.mazarin.spring.configuration;

import lk.mazarin.util.AuthenticatingUser;
import lk.mazarin.util.SecurityConfigResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.List;

/**
 * Created by Kasun Perera on 6/12/2016.
 */

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private SecurityConfigResource securityConfigProperty;

    public SecurityConfigResource getSecurityConfigProperty() {
        return securityConfigProperty;
    }

    @Autowired
    public void setSecurityConfigProperty(SecurityConfigResource securityConfigProperty) {
        this.securityConfigProperty = securityConfigProperty;
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {

        if (StringUtils.equalsIgnoreCase(securityConfigProperty.getProperty(SecurityConfigResource.AUTHENTICATING_USER_TYPE), "IN_MEMORY")){
            List<AuthenticatingUser> authenticatingUsers = securityConfigProperty.getAuthenticatingUsers();
            for (AuthenticatingUser user : authenticatingUsers){
                String[] roles = user.ROLE.split(",");
                for (int i=0; i<roles.length; ++i){
                    roles[i].trim();
                }
                auth.inMemoryAuthentication().withUser(user.USERNAME).password(user.PASSWORD).roles(roles);
            }
        }

//        auth.inMemoryAuthentication().withUser("bill").password("abc123").roles("USER");
//        auth.inMemoryAuthentication().withUser("admin").password("root123").roles("ADMIN");
//        auth.inMemoryAuthentication().withUser("dba").password("root123").roles("ADMIN","DBA");//dba have two roles.
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests().antMatchers();

        http.authorizeRequests()
                .antMatchers("/", "/home").permitAll()
                .antMatchers("/admin/**").access("hasRole('ADMIN')")
                .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
                .and().formLogin()
                .and().exceptionHandling().accessDeniedPage("/Access_Denied");

    }
}