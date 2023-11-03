import { useState } from "react";
import { getAllImages, uploadImage } from "./util";

interface IFileItem {
  fileDownloadUri: string;
  fileName: string;
  fileSize: number;
  fileUri: string;
  id: number;
  uploaderName: string;
}

const App = () => {
  const [files, setFiles] = useState<File[] | undefined>();
  const [uploader, setUploader] = useState("");

  const [allFiles, setAllFiles] = useState<IFileItem[] | undefined>();

  const onFileChange = (ev) => {
    setFiles(ev.target.files);
  };

  const onUploaderChange = (ev) => {
    setUploader(ev.target.value);
  };

  const onUpload = (ev) => {
    ev.preventDefault();
    const formData = new FormData();

    if (files) {
      for (let i = 0; i < files?.length; i++) {
        formData.append("files", files[i]);
      }
    }

    formData.append("name", uploader);

    uploadImage(formData)
      .then((res) => console.log(res))
      .catch((err) => console.log(err));
  };

  const onViewFiles = () => {
    getAllImages()
      .then((res) => setAllFiles(res.data))
      .catch((err) => console.log(err));
  };

  return (
    <div>
      <div>
        <p>select files:</p>
        <input
          type="file"
          name="files"
          multiple
          onChange={onFileChange}
        ></input>{" "}
      </div>
      <div>
        <p>uploaded by:</p>
        <input
          type="text"
          name="uploader"
          value={uploader}
          onChange={onUploaderChange}
        ></input>{" "}
      </div>
      <p>
        <button type="submit" onClick={onUpload}>
          upload
        </button>
        <button type="submit" onClick={onViewFiles}>
          view files
        </button>
      </p>

      <ol>
        {allFiles?.length === 0 && <p>no files uploaded as of now...</p>}
        {allFiles?.map((file) => (
          <li key={file.id}>
            <a
              href={file.fileDownloadUri}
              target="_blank"
              rel="noopener noreferrer"
            >
              {file.fileName}
            </a>
          </li>
        ))}
      </ol>
    </div>
  );
};

export default App;
