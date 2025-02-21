 **Aura - Social Media App**

 app/src/main/java/com/Keya/aura -navigate here to view the files in the repo

 **Project Synopsis**
Aura is a social media application designed to allow users to create profiles, follow other users, and share posts. The app provides seamless integration with Firebase for authentication and real-time database management, along with Cloudinary for image hosting. The objective is to offer an interactive and user-friendly platform where users can connect, share updates, and build an online presence.

**Objectives**
- To provide a platform where users can create profiles and connect with others.
- Enable users to upload and share posts with images.
- Implement a following system where users can follow and interact with others.
- Secure authentication using Firebase Authentication.
- Store and manage user data efficiently using Firebase Realtime Database.
- Optimize image storage and retrieval using Cloudinary.

---

 **Scope of the Project**
- **User Registration & Authentication**: Users can register using an email and password, and log in securely.
- **Profile Management**: Users can create and update their profiles, including profile pictures.
- **Post Creation**: Users can capture images using the camera or select from the gallery and upload them as posts.
- **Following System**: Users can follow/unfollow other users and view their posts.
- **Feed Display**: Users can view posts from the people they follow on their homepage.
- **Firebase Integration**: Real-time data updates for user information and posts.
- **Cloudinary Integration**: Used for efficient storage and retrieval of images.

---

**Technologies Used**
 **Frontend**
- Java (Android Studio)
- XML (UI Design)
- Glide (Image Loading)

### **Backend**
- Firebase Authentication (User authentication)
- Firebase Realtime Database (User data and posts storage)
- Cloudinary API (Image storage and retrieval)

**APIs & Services**
- **Firebase Authentication**: Handles user login and signup.
- **Firebase Realtime Database**: Stores user profiles, posts, followers, and following data.
- **Cloudinary API**: Uploads and retrieves images efficiently.

---

 **Project Structure**
- **LoginActivity.java**: Handles user login functionality.
- **RegisterActivity.java**: Manages user registration.
- **CreateProfile.java**: Allows users to set up their profile.
- **CreateNewPost.java**: Enables users to create and upload posts.
- **Follow_List.java**: Displays a list of users to follow.
- **FollowAdapter.java**: Adapter for managing the follow list in RecyclerView.
- **user_Homepage.java**: Displays the user feed containing posts from followed users.
- **UserAdapter.java**: Adapter for displaying user posts in the feed.

---

**Database Structure (Firebase Realtime Database)**
```
Users
 ├── userId
 │   ├── username: "JohnDoe"
 │   ├── email: "johndoe@example.com"
 │   ├── ProfileImageUrl: "https://cloudinary.com/..."
 │   ├── Followers: { userId1: true, userId2: true }
 │   ├── Following: { userId3: true, userId4: true }
 │   ├── posts
 │       ├── postId
 │       │   ├── postImageUrl: "https://cloudinary.com/..."
```

---

**How to Run the Project**
1. **Clone the Repository**:
   ```sh
   git clone <repository_url>
   cd Aura-Social-Media-App
   ```
2. **Open in Android Studio** and sync dependencies.
3. **Set up Firebase**:
   - Create a Firebase project and add the `google-services.json` file.
   - Enable Firebase Authentication (Email/Password).
   - Set up Firebase Realtime Database.
4. **Set up Cloudinary**:
   - Create a Cloudinary account.
   - Add API credentials to the project.
5. **Run the App on an Emulator or Device**.

---

 **Future Enhancements**
- Implement **like and comment** features on posts.
- Add **push notifications** for new followers and interactions.
- Improve UI with **better animations and themes**.
- Enhance security with **OAuth-based authentication**.

---

**Developed by Keya** 🚀

