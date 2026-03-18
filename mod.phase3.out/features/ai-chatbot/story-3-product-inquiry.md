### User Story

**Title:** Product Inquiry and Information Retrieval

**Story:** As a user, I want to ask the chatbot about specific products so that I can get information without searching the website.

**Acceptance Criteria:**
1.  The chatbot can identify when a user is asking about a product.
2.  The chatbot can extract the product name or keywords from the user's message.
3.  The chatbot queries a `ProductChatbotService` to get product information.
4.  The chatbot uses the retrieved product information to generate a helpful response.
5.  If the product is not found, the chatbot informs the user politely.