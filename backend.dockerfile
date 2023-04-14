FROM python:3.11.3-bullseye

WORKDIR /server
COPY ./requirements.txt ./
RUN pip install -r requirements.txt

COPY ./.env.example ./.env
COPY ./app/ ./app

EXPOSE 8000/tcp

CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000" ]