
const chatButton = document.getElementById('chat-button');
const chatWindow = document.getElementById('chat-window');
const closeChatButton = document.getElementById('close-chat');
const chatInput = document.getElementById('chat-input');
const sendChatButton = document.getElementById('send-chat');
const chatMessages = document.getElementById('chat-messages');

chatButton.addEventListener('click', () => {
    chatWindow.style.display = 'flex';
});

closeChatButton.addEventListener('click', () => {
    chatWindow.style.display = 'none';
});

sendChatButton.addEventListener('click', () => {
    sendMessage();
});

chatInput.addEventListener('keypress', (event) => {
    if (event.key === 'Enter') {
        sendMessage();
    }
});

function sendMessage() {
    const message = chatInput.value.trim();
    if (message) {
        // Display user message
        const userMessageDiv = document.createElement('div');
        userMessageDiv.style.cssText = 'background-color: #dcf8c6; padding: 8px; border-radius: 5px; margin-bottom: 5px; align-self: flex-end; text-align: right;';
        userMessageDiv.textContent = message;
        chatMessages.appendChild(userMessageDiv);

        chatInput.value = '';
        chatMessages.scrollTop = chatMessages.scrollHeight; // Scroll to bottom

        // Send message to backend
        fetch('/api/chatbot', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ message: message }),
        })
        .then(response => response.json())
        .then(data => {
            // Display bot response
            const botMessageDiv = document.createElement('div');
            botMessageDiv.style.cssText = 'background-color: #e0e0e0; padding: 8px; border-radius: 5px; margin-bottom: 5px; align-self: flex-start;';
            botMessageDiv.textContent = data.response;
            chatMessages.appendChild(botMessageDiv);
            chatMessages.scrollTop = chatMessages.scrollHeight; // Scroll to bottom
        })
        .catch(error => {
            console.error('Error:', error);
            const errorMessageDiv = document.createElement('div');
            errorMessageDiv.style.cssText = 'background-color: #ffdddd; padding: 8px; border-radius: 5px; margin-bottom: 5px; align-self: flex-start; color: red;';
            errorMessageDiv.textContent = 'Error: Could not get response from chatbot.';
            chatMessages.appendChild(errorMessageDiv);
            chatMessages.scrollTop = chatMessages.scrollHeight;
        });
    }
}
