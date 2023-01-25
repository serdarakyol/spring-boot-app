from locust import HttpUser, task, between
from locust.exception import RescheduleTask
import string
import random
import time
import datetime

WAIT_TIME_MIN = 0.1
WAIT_TIME_MAX = 0.5

h = {
    "Content-Type": "application/json"
}

random.seed()

class LoadTest(HttpUser):
    wait_time = between(WAIT_TIME_MIN, WAIT_TIME_MAX)
    abstract = True
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.path = None
        self.request_number = 0
        self.ids = []

    def generate_random_string(self, min_name_size=2, max_name_size=20) -> str:
        letters = string.ascii_lowercase
        string_size = random.randint(min_name_size, max_name_size)
        generated_string = ''.join(random.choice(letters) for i in range(string_size))
        return generated_string
    
    def generate_random_dob(self) -> str:
        d = random.randint(1, int(time.time()))
        return datetime.date.fromtimestamp(d).strftime('%Y-%m-%d')
    
    @task(1)
    def get_all(self):
        self.client.get(url=self.path)
    
    @task(8)
    def post_request(self):
        self.request_number += 1
        self.ids.append(self.request_number)
        self.client.post(url=self.path, json=self._generate_post_data(), headers=h)

    

class TeacherProcess(LoadTest):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.path = "/api/v1/teacher"
        
    def _generate_post_data(self):
        request_data = {
            "teacherName": str,
            "teacherEmail": str,
            "teacherDOB": str
        }
        request_data["teacherName"] = self.generate_random_string()
        request_data["teacherEmail"] = f"{self.generate_random_string()}@{self.generate_random_string()}.{self.generate_random_string()}"
        request_data["teacherDOB"] = self.generate_random_dob()

        return request_data


class StudentProcess(LoadTest):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.path = "/api/v1/student"

    def _generate_post_data(self):
        request_data = {
            "studentName": str,
            "studentEmail": str,
            "studentDOB": str
        }
        request_data["studentName"] = self.generate_random_string()
        request_data["studentEmail"] = f"{self.generate_random_string()}@{self.generate_random_string()}.{self.generate_random_string()}"
        request_data["studentDOB"] = self.generate_random_dob()

        return request_data

