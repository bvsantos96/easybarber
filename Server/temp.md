In this case, we need to build a custom permission evaluator that checks if the logged-in user is admin for the establishment ID in the path.
In this case, we need to build a custom permission evaluator that checks if the logged-in user is admin for the establishment ID in the path.

First, you specify an interface for checking permissions:
```java
public interface EstablishmentPermissionEvaluator {
    boolean hasAdminPermission(Authentication auth, Long establishmentId);
}
```

Then, you create an implementaion of this interface, which should query the database and check if the user has the 'admin' role for the specific establishment:
```java
@Service
public class EstablishmentPermissionEvaluatorImpl implements EstablishmentPermissionEvaluator {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean hasAdminPermission(Authentication auth, Long establishmentId) {
        User user = userRepository.findByUsername(auth.getName());
        // Here you should add the actual logic for checking if the user has a 'admin' role for the establishment
        // I'll assume that the User entity has a many-to-many relationship with Entity Establishment which also stores the role
        return user.getEstablishments().stream()
            .anyMatch(establishment ->
                establishment.getId().equals(establishmentId) &&
                establishment.getRole().equals("admin"));
    }
}
```

Then, you need to bind your custom permission evaluator to Spring’s expression handler:
```java
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    @Autowired 
    private EstablishmentPermissionEvaluator establishmentPermissionEvaluator;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler =
            new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator());
        return expressionHandler;
    }

    public class CustomPermissionEvaluator implements PermissionEvaluator {
        // You also need to implement the other methods from the PermissionEvaluator interface

        @Override
        public boolean hasPermission(
            Authentication auth, Object targetDomainObject, Object permission) {
            if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)) {
                return false;
            }
            String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();

            if (targetType.equals("ESTABLISHMENT")) {
                return establishmentPermissionEvaluator.hasAdminPermission(auth, (Long) targetDomainObject);
            }
            throw new UnsupportedOperationException("hasPermission is not supported for targetType " + targetType);
        }
    }
}
```

Now, finally, you can secure your methods like this:
```java
@PreAuthorize("hasPermission(#establishmentId, 'admin')")
public void adminOnlyMethod(Long establishmentId) {
    // ...
}
```
