# Story 3: UI Resilience & Caching

## Status
To Do

## Story
**As a** user,
**I want** to see clear feedback if the AI information is slow to load or fails, and experience fast page loads on subsequent visits,
**so that** I have a smooth and predictable experience.

## Acceptance Criteria
1. A loading indicator (e.g., a spinner or a "Loading...") is displayed on the product details page while the AI content is being fetched.
2. If the backend call to the `AiProductInfoService` fails or times out, a user-friendly error message is shown in place of the AI content section.
3. A caching mechanism is implemented in the `AiProductInfoService` to store results.
4. The cache has a defined expiration time (e.g., 24 hours).
5. Subsequent requests for the same product ID within the cache's time-to-live (TTL) return the cached data directly without calling the external AI service.

## Tasks / Subtasks
- **Task 1 (AC: 3, 4, 5):** Modify `AiProductInfoService` to include caching.
  - Add Spring's caching annotations (`@EnableCaching`, `@Cacheable`) to the `getAiProductInfo` method.
  - Configure a cache named "aiProductInfo" with a 24-hour TTL.
- **Task 2 (AC: 1):** Modify the `product-details.html` template to include a loading state.
  - This could be a simple text message or a CSS spinner that is displayed while the AI content is being fetched.
  - JavaScript might be needed to show/hide the loading indicator.
- **Task 3 (AC: 2):** Enhance the error handling in `product-details.html`.
  - Ensure the `aiProductInfoError` attribute passed from the controller is displayed clearly to the user.

## Dev Notes
- The epic mentions using a feature flag for the entire AI-related feature. While not part of this story, the developer should be aware of it.
- For caching, Spring's built-in caching support is sufficient. The developer should ensure that a cache manager (like Caffeine or EhCache) is added to the `pom.xml` if one is not already present.

## Change Log
| Date | Version | Description | Author |
|---|---|---|---|
| 2025-09-02 | 1.0 | Initial draft | Bob (Scrum Master) |

## Dev Agent Record
- No record yet.
