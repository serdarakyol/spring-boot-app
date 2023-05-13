from locust import task, between, FastHttpUser, HttpUser
import string
import random
import time
import datetime
import logging

WAIT_TIME_MIN = 0.1
WAIT_TIME_MAX = 0.5

h = {
    "Content-Type": "application/json",
    "Authorization": "Basic YWRtaW46YWRtaW4="
}

random.seed()

class LoadTest(HttpUser):
    wait_time = between(WAIT_TIME_MIN, WAIT_TIME_MAX)
    abstract = True
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.base_path = None

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
        self.client.get(url=self.base_path, headers=h)

    @task(1)
    def get_one_by_id(self):
        random_id = self.random_user_id()
        self.client.get(url=f"{self.base_path}/by-id/{random_id}", headers=h)

    @task(1)
    def get_one_by_email(self):
        random_email = self.random_user_mail()
        self.client.get(url=f"{self.base_path}/by-email/{random_email}", headers=h)

    @task(20)
    def post_request(self):
        self.client.post(url=self.base_path, json=self._generate_post_data(), headers=h)

    @task(4)
    def put_request(self):
        request_path, request_data = self._generate_put_data()
        self.client.put(url=request_path, json=request_data, headers=h)


class TeacherProcess(LoadTest):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.base_path = "/api/v1/teacher"
        self.teacher_mails = []
        self.counter = 0

    def on_start(self):
        self.client.post(url=self.base_path,
                         json=self._generate_post_data(),
                         headers=h)

    def random_user_mail(self) -> str:
        return random.choice(self.teacher_mails)

    def random_user_id(self) -> int:
        return random.randint(1, self.counter)

    def on_stop(self):
        for email in self.teacher_mails:
            with self.client.delete(url=f"{self.base_path}/by-email/{email}", headers=h, catch_response=True) as response:
                if response.status_code == 404:
                    logging.info(f"Teacher Fails: {self.base_path}/by-email/{email}")
        logging.info(f"Remaning mails: {len(self.teacher_mails)}")

    def _generate_post_data(self) -> dict:
        request_data = {
            "name" : self.generate_random_string(),
            "email": f"{self.generate_random_string()}@{self.generate_random_string()}.{self.generate_random_string()}",
            "dob": self.generate_random_dob()
        }
        self.teacher_mails.append(request_data["email"])
        self.counter += 1

        return request_data

    def _generate_put_data(self) -> (str | dict):
        request_data = {
            "name": self.generate_random_string(),
            "email": f"{self.generate_random_string()}@{self.generate_random_string()}.{self.generate_random_string()}",
            "dob": self.generate_random_dob()
        }

         # select random email on db
        random_teacher_mail_on_db = random.choice(self.teacher_mails)
        # delete randomly selected email from list
        self.teacher_mails.remove(random_teacher_mail_on_db)
        # add new email to the list
        self.teacher_mails.append(request_data["email"])

        return f"{self.base_path}/{random_teacher_mail_on_db}", request_data


class StudentProcess(LoadTest):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.base_path = "/api/v1/student"
        self.student_mails = []
        self.counter = 0

    def on_start(self):
        self.client.post(url=self.base_path,
                         json=self._generate_post_data(),
                         headers=h)
        
    def random_user_mail(self) -> str:
        return random.choice(self.student_mails)

    def random_user_id(self) -> int:
        return random.randint(1, self.counter)

    def on_stop(self):
        for email in self.student_mails:
            with self.client.delete(url=f"{self.base_path}/by-email/{email}", headers=h, catch_response=True) as response:
                if response.status_code == 404:
                    logging.info(f"Teacher Fails: {self.base_path}/by-email/{email}")
        logging.info(f"Remaning mails: {len(self.student_mails)}")

    def _generate_post_data(self) -> dict:
        request_data = {
            "name": self.generate_random_string(),
            "email": f"{self.generate_random_string()}@{self.generate_random_string()}.{self.generate_random_string()}",
            "dob": self.generate_random_dob()
        }

        self.student_mails.append(request_data["email"])
        self.counter += 1

        return request_data

    def _generate_put_data(self) -> (str | dict):
        request_data = {
            "name": self.generate_random_string(),
            "email": f"{self.generate_random_string()}@{self.generate_random_string()}.{self.generate_random_string()}",
            "dob": self.generate_random_dob()
        }

        # select random email on db
        random_student_mail_on_db = random.choice(self.student_mails)
        # delete randomly selected email from list
        self.student_mails.remove(random_student_mail_on_db)
        # add new email to the list
        self.student_mails.append(request_data["email"])

        return f"{self.base_path}/{random_student_mail_on_db}", request_data
