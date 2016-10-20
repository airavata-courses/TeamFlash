FROM ubuntu:14.04
RUN apt-get update
RUN DEBIAN_FRONTEND=noninteractive apt-get install -y -q python-all python-pip 
ADD ./StormDetection/requirements.txt /tmp/requirements.txt
RUN pip install -qr /tmp/requirements.txt
ADD ./StormDetection /opt/StormDetection/
WORKDIR /opt/StormDetection
EXPOSE 7000
CMD ["python", "StormDetection.py"]
