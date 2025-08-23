# Prompts

```logs
/p 
check git logs, local changes that are not yet committed, run an end-2-end test, and then give me a summary of what phases and sub-stages have you completed and what is pending. refer to mod2.out folder for the sample code, these modules worked fine there
/p 
follow the instruction and start modernizing the shopping-cart app
/p
for those tests with redirection, please change the tests to look at the logs for the redirection action, and avoid waiting for the rediction to actually happen, mark the test limitations and move on.
/p
refer to the code in mod2.out, there are similar elements in there with the solutions that you are solving.
```

# Points

```logs
1. Certain tests were proven to be complex and expensive for the agent to resolve. For examples, GUI redirection tests, which has different delay depending on the underlying logics and gui components. It's logically to mark those tests as limitations and move on instead of pushing the agent.
1. The agent does not have the memory of the previous solutions, or might not have the ability to search widely for alternate solutions. For example, the issue with chromedirver in selenium tests. I would attempt to point it to specific solution as a reference to assist with the current problems. This has been proven to be able to unblock the agent.
```
                                       