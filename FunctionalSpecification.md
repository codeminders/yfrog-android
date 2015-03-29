## Functional specification for v2.0 ##

### Description ###

The application is iPhone client for Twitter.com service, which extends its functionality with image and video posting and display functions, powered by yfrog.com web site. In addition to Twitter and yFrog functionality, it also makes use of iPhone GPS to provide some additional services.
Features

### Twitter features ###

  1. Log in with twitter account with option "remember me".
  1. Support Multiple Twitter accounts.
  1. Option to log out and forget remembered password.
  1. Show last messages from people you "follow" on twitter (aka "Timeline").
  1. Show twitter user info for any user (avatar, profile information, location, etc.).
  1. Show messages from any user (e.g. anybody referenced via @username or by typing his twitter name).
  1. Browse my own tweets.
  1. Show and send @replies.
  1. Show and send direct messages.
  1. Show avatar pics of twitter users (need to cache them for performance reasones).
  1. Allow follow/unfollow other users.
  1. To be able to browse other user followers.
  1. Search Function and Saved Searches (see Tweetie iPhone app for Example).
  1. It should be possible to follow/unfollow users.
  1. It should be possible to change notification settings per user (whenever twitter should send SMS with their new messages)
  1. Application code should be fully localizable. Translate to Russian as example.
  1. Support OAUth (requires launch of Web Browser on device) or Username/Password authentication.

### yFrog/image and video handling features ###

  1. Send status updates to Twitter (tweets)
  1. While sending new tweet, option to take picture or video, upload to yfrog and include URL in tweet.
  1. When friends tweet contains yfrog.com URL, download and show it as embedded thumbnail (for video as well).
  1. Allow to "Zoom" by clicking to thumb in tweet and to see full size picture (with zoom and ability to save it to photo album)
  1. While sending new tweet, option upload existing picture or video (from photo album or storage folder) , upload to yfrog and include URL in tweet.
  1. For yfrog.com pictures which contain coordinates show "MAP" button which opens map and shows where it was taken (use Map application on device or open browser with google maps URL)
  1. Should be able to send pictures and location URLs in direct messages as we do in regular ones.

### Device-specfic features ###

  1. Optionally include GPS coordinates with picture you post (if GPS present, got position fix and user enabled it in settings)
  1. Option to change your "location" in Twitter with each post
  1. Optionally include link to your current location map (google map URL) in tweet via button
  1. Work in horizontal and vertical mode (if device supports such modes)
  1. "Forward" tweet by email
  1. "Forward" tweet by SMS

### Misc. ###

When posting tweets, replies, direct messages with images or without, it should be possible to queue them and post as batch later.
User should be able to enter and see information (tweets) in any language supported by device and Twitter. (Unicode)

### Technical Details ###

Yfrog operations should be developed as stand-alone library which we will offer as open-source. It should be clean and well documented.