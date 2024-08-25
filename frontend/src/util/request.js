import axios from 'axios'

// create an axios instance
const service = axios.create({
    baseURL: "http://localhost:8080"
})

export default service
