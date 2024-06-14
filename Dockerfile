FROM tensorflow/tensorflow:latest-gpu-jupyter
LABEL authors="angger"

RUN curl -fsSL https://nvidia.github.io/libnvidia-container/gpgkey | gpg --dearmor -o /usr/share/keyrings/nvidia-container-toolkit-keyring.gpg \
  && curl -s -L https://nvidia.github.io/libnvidia-container/stable/deb/nvidia-container-toolkit.list | \
    sed 's#deb https://#deb [signed-by=/usr/share/keyrings/nvidia-container-toolkit-keyring.gpg] https://#g' | \
    tee /etc/apt/sources.list.d/nvidia-container-toolkit.list

RUN sed -i -e '/experimental/ s/^#//g' /etc/apt/sources.list.d/nvidia-container-toolkit.list

RUN apt-get update -y
RUN apt-get install -y nvidia-container-toolkit

RUN nvidia-ctk runtime configure --runtime=docker

RUN pip3 install --upgrade pip
RUN pip3 install \
    numpy \
    pandas \
    geopandas \
    geopy \
    matplotlib \
    seaborn \
    scikit-learn \
    textblob 