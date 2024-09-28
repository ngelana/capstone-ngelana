FROM tensorflow/tensorflow

ENV PYTHONUNBUFFERED True
ENV APP_HOME /app

WORKDIR $APP_HOME

COPY requirements.txt ./
RUN pip install --no-cache-dir --upgrade -r requirements.txt

COPY . ./

EXPOSE 4000

CMD ["gunicorn", "--bind", "0.0.0.0:4000", "api.main:app"]