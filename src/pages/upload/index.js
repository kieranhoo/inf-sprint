import { useState } from 'react';
import { ref, uploadBytesResumable, getDownloadURL } from 'firebase/storage';
import { storage } from '../../firebase';
//import "./style.css";

const UploadPage = () => {
    const [progress, setProgress] = useState(0);
    const [title, setTitle] = useState();
    const [version, setVerion] = useState();
    const [description, setDescription] = useState();
    const [category, setCategory] = useState();
    const [message, setMessage] = useState();
    const [url, setUrl] = useState();  

    const formHandler = (e) => {
        e.preventDefault();
        const file = document.getElementById("file-upload").files[0];
        uploadFiles(file);
    };

    const uploadFilesToDD = async () => {
        try {
          let res = await fetch("https://httpbin.org/post", {
            method: "POST",
            body: JSON.stringify({
              title: title,
              version: version,
              description: description,
              category: category,
              url: url,
            }),
          });
          let resJson = await res.json();
          console.log(resJson);
          if (res.status === 200) {
            setTitle("");
            setVerion("");
            setDescription("");
            setCategory("");
            setUrl("");
            setMessage("File upload successfully");
          } else {
            setMessage("Some error occured");
          }
        } catch (err) {
          console.log(err);
        }
      }    

    const uploadFiles = (file) => {
        if (!file) return;

        // Create a storage reference from our storage service
        const storageRef = ref(storage, `files/${file.name}`);
        const uploadTask = uploadBytesResumable(storageRef, file);

        uploadTask.on(
            'state_changed',
            (snapshot) => {
                const prog = Math.round(
                (snapshot.bytesTransferred / snapshot.totalBytes) * 100
            );
            setProgress(prog);
        }, 
        (error) => console.error(error),
        () => {
            getDownloadURL(uploadTask.snapshot.ref).then((geturl) => {
            console.log('File available at:', geturl);
            setUrl(geturl);
            uploadFilesToDD();
            console.log('Download Url:', geturl);
            });
        }
        );
    };

    return (
        <div className="App">
            
        <section class="max-w-4xl p-6 mx-auto bg-indigo-500 rounded-md shadow-md dark:bg-gray-800 mt-20">
            <h1 class="text-xl font-bold text-white capitalize dark:text-white">Upload File</h1>
            <form  onSubmit={formHandler}>
                <div class="grid grid-cols-1 gap-6 mt-4 sm:grid-cols-2">
                    <div>
                        <label class="text-white dark:text-gray-200" for="title">Title</label>
                        <input id="title" type="text" onChange={(e) => setTitle(e.target.value)} class="block w-full px-4 py-2 mt-2 text-gray-700 bg-white border border-gray-300 rounded-md dark:bg-gray-800 dark:text-gray-300 dark:border-gray-600 focus:border-blue-500 dark:focus:border-blue-500 focus:outline-none focus:ring"/>
                    </div>

                    <div>
                        <label class="text-white dark:text-gray-200" for="category">Category</label>
                        <input id="category" type="text" onChange={(e) => setCategory(e.target.value)} class="block w-full px-4 py-2 mt-2 text-gray-700 bg-white border border-gray-300 rounded-md dark:bg-gray-800 dark:text-gray-300 dark:border-gray-600 focus:border-blue-500 dark:focus:border-blue-500 focus:outline-none focus:ring"/>
                    </div>
                </div>
                <br/>
                <div> 
                    <label class="text-white dark:text-gray-200" for="description">Description</label>
                    <textarea id="description" type="textarea" onChange={(e) => setDescription(e.target.value)} class="block w-full px-4 py-2 mt-2 text-gray-700 bg-white border border-gray-300 rounded-md dark:bg-gray-800 dark:text-gray-300 dark:border-gray-600 focus:border-blue-500 dark:focus:border-blue-500 focus:outline-none focus:ring"></textarea>
                </div>
                <br/>
                <div>
                    <label class="block text-sm font-medium text-white">
                        File
                    </label>
                    <div class="mt-1 flex justify-center px-6 pt-5 pb-6 border-2 border-gray-300 border-dashed rounded-md">
                        <div class="space-y-1 text-center">
                            <svg class="mx-auto h-12 w-12 text-white" stroke="currentColor" fill="none" viewBox="0 0 48 48" aria-hidden="true">
                                <path d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                            </svg>
                            <div class="flex text-sm text-gray-600">
                            <input id="file-upload" name="file-upload" type="file" class="block w-full text-sm text-gray-900 border border-gray-300 rounded-lg cursor-pointer bg-gray-50 dark:text-gray-400 focus:outline-none dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400" multiple/>
                                {/* <label for="file-upload" class="relative cursor-pointer bg-white rounded-md font-medium text-indigo-600 hover:text-indigo-500 focus-within:outline-none focus-within:ring-2 focus-within:ring-offset-2 focus-within:ring-indigo-500">
                                <span class="">Upload a file</span>
                                
                                </label> */}
                            </div>
                        </div>
                    </div>
                </div>
                
                <br/>
                    <h2 className='text-white'>Uploading done {progress}%</h2>
                    <div className="message text-white">{progress === 100 ? <p>{message}</p> : null}</div>
                
                <div class="flex justify-end mt-6">
                    <button class="px-6 py-2 leading-5 text-white transition-colors duration-200 transform bg-pink-500 rounded-md hover:bg-pink-700 focus:outline-none focus:bg-gray-600">Save</button>
                </div>
            </form>
        </section>
            
            {/* <form onSubmit={formHandler}>
                <input
                type="text"
                value={title}
                placeholder="Title"
                onChange={(e) => setTitle(e.target.value)}
                />
                <input
                type="text"
                value={description}
                placeholder="Description"
                onChange={(e) => setDescription(e.target.value)}
                />
                <input
                type="text"
                value={version}
                placeholder="Dersion"
                onChange={(e) => setVerion(e.target.value)}
                />
                <input
                type="text"
                value={category}
                placeholder="Category"
                onChange={(e) => setCategory(e.target.value)}
                />
                <hr />
                <input type="file" className="input" />
                <button type="submit">Upload</button>
                <h2>Uploading done {progress}%</h2>
                <div className="message">{progress === 100 ? <p>{message}</p> : null}</div>
            </form> */}
        </div>
    );
}

export default UploadPage