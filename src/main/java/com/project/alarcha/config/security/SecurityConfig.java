package com.project.alarcha.config.security;

import com.project.alarcha.service.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable().cors().and()
                .authorizeRequests()
                //user
                .antMatchers("/user/sign-in").permitAll()
                .antMatchers("/user/refreshtoken").permitAll()
                .antMatchers( "/user/sign-up").permitAll()
                .antMatchers( "/user/delete/{userId}").hasAuthority("SUPER_ADMIN")
                .antMatchers( "/user/get-admins").hasAuthority("SUPER_ADMIN")
                .antMatchers( "/user/get/my-orders/{id}").hasAuthority("CLIENT")
                //area
                .antMatchers("/area/**").hasAuthority("SUPER_ADMIN")
                //hotel
                .antMatchers("/hotel/create").hasAuthority("SUPER_ADMIN")
                .antMatchers("/hotel/get-for-list").hasAuthority("SUPER_ADMIN")
                .antMatchers("/hotel/delete/{hotelId}").hasAuthority("SUPER_ADMIN")
                //roomType
                .antMatchers("/room/create").hasAuthority("SUPER_ADMIN")
                .antMatchers("/room/type/delete/{roomTypeId}").hasAuthority("SUPER_ADMIN")
                .antMatchers("/room/type/get-for-list").hasAuthority("SUPER_ADMIN")
                //room
                .antMatchers("/room/type/get-for-list").hasAuthority("SUPER_ADMIN")
                .antMatchers("/room/delete/{roomTypeId}").hasAuthority("SUPER_ADMIN")
                .antMatchers("/room/get-for-list").hasAuthority("SUPER_ADMIN")
                //roomOrder
                .antMatchers("/room/order").hasAuthority("CLIENT")
//                .antMatchers("/room/order/pay").hasAuthority("CLIENT")
//                .antMatchers( "/room/order/{roomOrderId}/accept").hasAuthority("ADMIN")
//                .antMatchers("/room/order/{roomOrderId}/decline").hasAuthority("ADMIN")
//                .antMatchers("/room/order/{roomOrderId}/accept-pay").hasAuthority("ADMIN")
//                .antMatchers("/room/order/delete/{roomOrderId}").hasAuthority("ADMIN")
                .antMatchers("/room/order/get-all").hasAuthority("ADMIN")
                .antMatchers("/room/order/get-confirmed-or-declined").hasAuthority("ADMIN")
                .antMatchers("/room/order/get-in-process").hasAuthority("ADMIN")
                .antMatchers("/room/order/get-in-pay-check").hasAuthority("ADMIN")
                .antMatchers("/room/order/get/{roomOrderId}").hasAnyAuthority("ADMIN", "CLIENT")
                //hotelHall
                .antMatchers("/hotelHall/create").hasAuthority("SUPER_ADMIN")
                .antMatchers("/hotelHall/delete/{hotelHallId}").hasAuthority("SUPER_ADMIN")
                .antMatchers("/hotelHall/get-for-list").hasAuthority("SUPER_ADMIN")
                //hotelHallOrder
                .antMatchers("/hotelHall/order").hasAuthority("CLIENT")
                .antMatchers("/hotelHall/order/pay").hasAuthority("CLIENT")
//                .antMatchers("/hotelHall/order/{hotelHallOrderId}/accept").hasAuthority("ADMIN")
//                .antMatchers("/hotelHall/order/{hotelHallOrderId}/decline").hasAuthority("ADMIN")
//                .antMatchers("/hotelHall/order/{hotelHallOrderId}/accept-pay").hasAuthority("ADMIN")
//                .antMatchers("/hotelHall/order/delete/{hotelHallOrderId}").hasAuthority("ADMIN")
                .antMatchers("/hotelHall/order/get/{hotelHallOrderId}").hasAnyAuthority("ADMIN", "CLIENT")
                .antMatchers("/hotelHall/order/get-all").hasAuthority("ADMIN")
                .antMatchers("/hotelHall/order/get-in-process").hasAuthority("ADMIN")
                .antMatchers("/hotelHall/order/get-confirmed-or-declined").hasAuthority("ADMIN")
                .antMatchers("/hotelHall/order/get-in-pay-check").hasAuthority("ADMIN")
                .antMatchers("/hotelHall/order/get-paid").hasAuthority("ADMIN")
                //object
                .antMatchers("/object/create").hasAuthority("SUPER_ADMIN")
                .antMatchers("/object/delete/{objectId}").hasAuthority("SUPER_ADMIN")
                .antMatchers("/object/get-for-list").hasAuthority("SUPER_ADMIN")
                //objectOrder
                .antMatchers("/object/order").hasAuthority("CLIENT")
                .antMatchers("/object/order/pay").hasAuthority("CLIENT")
//                .antMatchers("/object/order/{objectOrderId}/accept").hasAuthority("ADMIN")
//                .antMatchers("/object/order/{objectOrderId}/decline").hasAuthority("ADMIN")
//                .antMatchers("/object/order/{objectOrderId}/accept-pay").hasAuthority("ADMIN")
//                .antMatchers("/object/order/delete/{objectOrderId}").hasAuthority("ADMIN")
                .antMatchers("/object/order/get-all").hasAuthority("ADMIN")
                .antMatchers("/object/order/get-in-process").hasAuthority("ADMIN")
                .antMatchers("/object/order/get-in-pay-check").hasAuthority("ADMIN")
                .antMatchers("/object/order/get-paid").hasAuthority("ADMIN")
                .antMatchers("/object/order/get-confirmed-or-declined").hasAuthority("ADMIN")
                .antMatchers("/object/order/get/{objectOrderId}").hasAnyAuthority("ADMIN", "CLIENT")
                //objectType
                .antMatchers("/object/type/create").hasAuthority("SUPER_ADMIN")
                .antMatchers("/object/type/get-for-list").hasAuthority("SUPER_ADMIN")
                .antMatchers("/object/type/delete/{objectTypeId}").hasAuthority("SUPER_ADMIN")
                //menuSection
                .antMatchers("/menu/section/create").hasAuthority("SUPER_ADMIN")
                .antMatchers("/menu/section/delete/{menuSectionId}").hasAuthority("SUPER_ADMIN")
                .antMatchers("/menu/section/get-for-list").hasAuthority("SUPER_ADMIN")
                //menu
                .antMatchers("/menu/create").hasAuthority("SUPER_ADMIN")
                .antMatchers("/menu/delete/{menuId}").hasAuthority("SUPER_ADMIN")
                //natureType
                .antMatchers("/nature/type/create").hasAuthority("SUPER_ADMIN")
                .antMatchers("/nature/type/create-flora-and-fauna").hasAuthority("SUPER_ADMIN")
                .antMatchers("/nature/type/delete/{id}").hasAuthority("SUPER_ADMIN")
                //nature
                .antMatchers("/nature/create").hasAuthority("SUPER_ADMIN")
                .antMatchers("/nature/delete/{id}").hasAuthority("SUPER_ADMIN")
                .antMatchers("/nature/get-for-list").hasAuthority("SUPER_ADMIN")
                //feedback
                .antMatchers("/reply").hasAuthority("SUPER_ADMIN")
                .antMatchers("/get-all").hasAuthority("SUPER_ADMIN")
                .anyRequest().permitAll();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userSecurityService);
        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

}