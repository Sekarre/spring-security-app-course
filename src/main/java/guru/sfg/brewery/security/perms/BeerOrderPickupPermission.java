package guru.sfg.brewery.security.perms;


import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@PreAuthorize("hasAuthority('order.pickup') OR" +
        " hasAuthority('customer.order.pickup') AND " +
        "@beerOrderAuthenticationManager.customerIdMatches(authentication, #customerId)")
@Retention(RetentionPolicy.RUNTIME)
public @interface BeerOrderPickupPermission {
}
