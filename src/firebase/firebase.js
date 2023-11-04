import { initializeApp } from 'firebase/app';
import { getStorage } from 'firebase/storage';

const firebaseConfig = {
  apiKey: 'AIzaSyDtjjlM6H_XsgkI9lWwIGRaq032GBapzcQ',
  authDomain: 'kotlin-ecommerce-a7e7c.firebaseapp.com',
  projectId: 'kotlin-ecommerce-a7e7c',
  storageBucket: 'kotlin-ecommerce-a7e7c.appspot.com',
  messagingSenderId: '100328108842',
  appId: '1:100328108842:web:94b0fd919d91f43e6ee453',
  measurementId: 'G-VZ7Q4KZBQ3',
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

// Get a reference to the storage service
const storage = getStorage(app);

export { storage };
