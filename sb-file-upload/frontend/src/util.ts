import axios from "axios";

const BASE_URL = "http://localhost:8080/file";

export const getAllImages = () => {
  return axios.get(BASE_URL);
};

export const uploadImage = (formData: FormData) => {
  return axios.post(BASE_URL + "/upload", formData);
};