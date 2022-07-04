1) Skipped auth, because it requires  a lot of code and is not required for the task
2) Unfortunately, hibernate does not support kotlin out of the box, so code in that place is a bit clumsy
3) I did not have enough time to add more test cases for concurrent transaction execution, maybe I will do it later

We can implement different strategies to handle concurrent transactions, but simplest one is just to cancel transaction if one is already in progress.
Our main goal is to leave DB in consistent state, for this we will use optimistic locking.

Test is also a little clumsy, but it works. Removing version column clearly shows the error.