from locust import HttpUser, task, between
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
        self.path_all = None
        self.path_one = None

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
        self.client.get(url=self.path_all)

    @task(100)
    def post_request(self):
        self.client.post(url=self.path_all, json=self._generate_post_data(), headers=h)

    @task(2)
    def put_request(self):
        self.client.put(url=self._generate_put_data())


class TeacherProcess(LoadTest):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.path_all = "/api/v1/teacher"
        self.counter = 0
        self.ids = []

    def on_start(self):
        self.client.post(url=self.path_all,
                         json=self._generate_post_data(),
                         headers=h)

    def _generate_post_data(self) -> dict:
        request_data = {
            "teacherName": str,
            "teacherEmail": str,
            "teacherDOB": str
        }
        request_data["teacherName"] = self.generate_random_string()
        request_data["teacherEmail"] = f"{self.generate_random_string()}@{self.generate_random_string()}.{self.generate_random_string()}"
        request_data["teacherDOB"] = self.generate_random_dob()
        self.counter += 1
        self.ids.append(self.counter)

        return request_data

    def _generate_put_data(self) -> str:
        if len(self.ids) > 0:
            teacher_name = self.generate_random_string()
            teacher_email = f"{self.generate_random_string()}@{self.generate_random_string()}.{self.generate_random_string()}"
            teacher_email = teacher_email.replace("@", "%40")
            teacher_id = str(random.choice(self.ids))
            request_string = f"{self.path_all}/{teacher_id}?teacherName={teacher_name}&teacherEmail={teacher_email}"
                
            return request_string
        # this is added for handle None returns
        return f"{self.path_all}/1?teacherName=ibjy&teacherEmail=et%40bedhaxkfv.fndxldvtrkdsjcjiwit"


class StudentProcess(LoadTest):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.path_all = "/api/v1/student"
        self.counter = 0
        self.ids = []
    
    def on_start(self):
        self.client.post(url=self.path_all,
                         json=self._generate_post_data(),
                         headers=h)

    def _generate_post_data(self) -> dict:
        request_data = {
            "studentName": str,
            "studentEmail": str,
            "studentDOB": str
        }
        request_data["studentName"] = self.generate_random_string()
        request_data["studentEmail"] = f"{self.generate_random_string()}@{self.generate_random_string()}.{self.generate_random_string()}"
        request_data["studentDOB"] = self.generate_random_dob()
        self.counter += 1
        self.ids.append(self.counter)

        return request_data
    
    def _generate_put_data(self) -> str:
        if len(self.ids) > 0:
            student_name = self.generate_random_string()
            student_email = f"{self.generate_random_string()}@{self.generate_random_string()}.{self.generate_random_string()}"
            student_email = student_email.replace("@", "%40")
            student_id = str(random.choice(self.ids))
            request_string = f"{self.path_all}/{student_id}?studentName={student_name}&studentEmail={student_email}"

            return request_string
        # this is added for handle None returns
        return f"{self.path_all}/1?studentName=ibjy&studentEmail=et%40bedhaxkfv.fndxldvtrkdsjcjiwit"
