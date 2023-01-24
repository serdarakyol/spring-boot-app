from locust import HttpUser, task, between
import string
import random
import time
import datetime

WAIT_TIME_MIN = 1
WAIT_TIME_MAX = 5

h = {
    "Content-Type": "application/json"
}

random.seed()

class LoadTest(HttpUser):
    wait_time = between(WAIT_TIME_MIN, WAIT_TIME_MAX)
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.student_path = "/api/v1/student"
        self.teacher_path = "/api/v1/teacher"
        self.student_request_number = 0
        self.teacher_request_number = 0

    def generate_random_string(self, min_name_size=2, max_name_size=20) -> str:
        letters = string.ascii_lowercase
        string_size = random.randint(min_name_size, max_name_size)
        generated_string = ''.join(random.choice(letters) for i in range(string_size))
        return generated_string
    
    def generate_random_dob(self) -> str:
        d = random.randint(1, int(time.time()))
        return datetime.date.fromtimestamp(d).strftime('%Y-%m-%d')
    
    def generate_student_post_data(self):
        request_data = {
            "studentName": str,
            "studentEmail": str,
            "studentDOB": str
        }
        request_data["studentName"] = self.generate_random_string()
        request_data["studentEmail"] = f"{self.generate_random_string()}@{self.generate_random_string()}.{self.generate_random_string()}"
        request_data["studentDOB"] = self.generate_random_dob()

        return request_data
    
    def generate_teacher_post_data(self):
        request_data = {
            "teacherName": str,
            "teacherEmail": str,
            "teacherDOB": str
        }
        request_data["teacherName"] = self.generate_random_string()
        request_data["teacherEmail"] = f"{self.generate_random_string()}@{self.generate_random_string()}.{self.generate_random_string()}"
        request_data["teacherDOB"] = self.generate_random_dob()

        return request_data

    @task(10)
    def get_all_student(self):
        self.student_request_number += 1
        self.client.get(url=self.student_path)

    @task(10)
    def get_all_teacher(self):
        self.teacher_request_number += 1
        self.client.get(url=self.teacher_path)

    @task(40)
    def student_post_request(self):
        self.student_request_number += 1
        self.client.post(url=self.student_path, json=self.generate_student_post_data(), headers=h)
    
    @task(40)
    def teacher_post_request(self):
        self.teacher_request_number += 1
        self.client.post(url=self.teacher_path, json=self.generate_teacher_post_data(), headers=h)
