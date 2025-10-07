# API Fixes Applied

## Issues Found and Fixed

### 1. Hibernate Lazy Loading Serialization Error

**Problem:** When returning entities with lazy-loaded relationships, Hibernate creates proxy objects that Jackson (the JSON serializer) cannot serialize, resulting in:
```
Type definition error: [simple type, class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor]
```

**Solution:** Added `@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})` annotation to all entity classes:
- `Project.java`
- `Sprint.java`
- `Task.java`
- `Organization.java`
- `User.java`

This tells Jackson to ignore Hibernate's internal proxy properties when serializing entities to JSON.

---

### 2. Date Format Mismatch

**Problem:** The Sprint entity uses `LocalDateTime` for start and end dates, but the API was accepting date strings in `YYYY-MM-DD` format, causing:
```
Cannot deserialize value of type `java.time.LocalDateTime` from String "2025-10-07"
```

**Solution:** 
1. Changed `SprintRequest.java` DTO to use `LocalDate` instead of `LocalDateTime`
2. Updated `SprintController.java` to convert `LocalDate` to `LocalDateTime`:
   - Start dates: Convert to beginning of day using `.atStartOfDay()`
   - End dates: Convert to end of day using `.atTime(23, 59, 59)`

This allows the API to accept simple date formats while maintaining the internal `LocalDateTime` representation.

---

### 3. Task Status and Priority Format Corrections

**Problem:** Test scripts were using incorrect values for task status and priority:
- Status was using `TODO` instead of `TO_DO`
- Priority was using strings like `"HIGH"` instead of integers

**Solution:** Updated test scripts to use correct values:
- **Status values**: `TO_DO`, `IN_PROGRESS`, `DONE`, `BLOCKED`
- **Priority values**: Integers from 1 (lowest) to 5 (highest)

---

## Files Modified

### Entity Models
- `/src/main/java/com/dbelanger/spring/agileapi/model/Project.java`
- `/src/main/java/com/dbelanger/spring/agileapi/model/Sprint.java`
- `/src/main/java/com/dbelanger/spring/agileapi/model/Task.java`
- `/src/main/java/com/dbelanger/spring/agileapi/model/Organization.java`
- `/src/main/java/com/dbelanger/spring/agileapi/model/User.java`

### DTOs
- `/src/main/java/com/dbelanger/spring/agileapi/dto/SprintRequest.java`

### Controllers
- `/src/main/java/com/dbelanger/spring/agileapi/controller/SprintController.java`

### Documentation & Tests
- `/docs/test-endpoints.sh`
- `/docs/test-commands.md`

---

## Testing

To test the fixes:

1. Restart your Spring Boot application
2. Run the automated test script:
   ```bash
   chmod +x docs/test-endpoints.sh
   ./docs/test-endpoints.sh
   ```

Or use individual commands from `docs/test-commands.md`.

---

## Expected Behavior After Fixes

All endpoints should now work correctly:
- ✅ Projects can be created, retrieved, updated, and deleted
- ✅ Sprints can be created with date-only values (YYYY-MM-DD format)
- ✅ Tasks can be created with proper status enums and integer priorities
- ✅ All lazy-loaded relationships serialize properly to JSON
- ✅ No more Hibernate proxy serialization errors
