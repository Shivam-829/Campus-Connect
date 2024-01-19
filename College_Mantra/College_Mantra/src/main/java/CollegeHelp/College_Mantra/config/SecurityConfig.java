package CollegeHelp.College_Mantra.config;

import CollegeHelp.College_Mantra.enums.Authority;
import CollegeHelp.College_Mantra.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/signup/**").permitAll()
                .antMatchers("/college/**").permitAll()
                .antMatchers("/student/**").hasAuthority(Authority.STUDENT.toString())
                .antMatchers("/roomOwner/**").hasAuthority(Authority.ROOM_HONOUR.toString())
                .antMatchers("/mess/**").hasAuthority(Authority.MESS.toString())
                .antMatchers("/serviceProvider/**").hasAuthority(Authority.SERVICE_PROVIDER.toString())
                .anyRequest().fullyAuthenticated()
                .and()
                .httpBasic();
    }
}
