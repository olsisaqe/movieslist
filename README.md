# movieslist

Small considerations: 

I did try totally new things in the test task since It was a good time for some experiments for me. 

I did use ktor client for network layer and coupled everything with coroutines instead of RxJava. 
The result is quite nice I have to say. 
The testing of coroutines is a little bit more tricky but didn’t cause big troubles. 

Overall the ktor client instead of retrofit is a very good alternative (especially when you want to use coroutines). Also kotlin serialisation for JSON parsing is very awesome. 

With this architecture in the network layer every part of code is kotlin only. This means that the network layer can easily become a multiplatform module to share core business logic between iOS and Android. 
