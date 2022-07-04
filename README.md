1) Skipped auth, because it requires  a lot of code and is not required for the task
2) Unfortunately, hibernate does not support kotlin out of the box, so code in that place is a bit clumsy

We can implement different strategies to handle concurrent transactions, but simplest one is just to cancel transaction if one is already in progress.
Our main goal is to leave DB in consistent state, for this we will use optimistic locking.

To verify concurrency test we can remove version column and error will occur.