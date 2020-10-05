package guru.sfg.brewery.config;

import guru.sfg.brewery.security.JpaUserDetailsService;
import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestUrlAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);

        return filter;
    }

    public RestUrlAuthFilter restUrlAuthFilter(AuthenticationManager authenticationManager) {
        RestUrlAuthFilter filter = new RestUrlAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);

        return filter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(restHeaderAuthFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();

        http.addFilterBefore(restUrlAuthFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class);

        http
                .authorizeRequests(authorize -> {
                    authorize
                            .mvcMatchers("/h2-console/**").permitAll() //h2 console
                            .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                            .antMatchers("/beers/find", "/beers*").permitAll()
                            .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
                            .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll();
                })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();

        //h2 console
        http.headers().frameOptions().sameOrigin();
    }


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

//        auth.inMemoryAuthentication()
//                .withUser("admin")
//                .password("{bcrypt}$2a$10$d7Rud4B7c9TMzc1KkeR.LuahaNu.f2IU0pF5keNVzXIXkGW9o4D0.")
//                .roles("ADMIN")
//                .and()
//                .withUser("user")
//                .password("{sha256}7093bfeb2107afa0cca25af193af41bcd8687c68fba7aaf6b29c2b8d570a1212c706bc7423f7d197")
//                .roles("USER");
//
//        auth.inMemoryAuthentication().withUser("scott").password("{bcrypt10}$2a$15$inv2jWPJeuj/1w4d75t/juIM3DnWAUkxXkB4Tc/s0HMfPr9GugkuK").roles("CUSTOMER");
//    }
}
