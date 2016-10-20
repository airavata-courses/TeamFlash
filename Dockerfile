FROM ubuntu:14.04
RUN apt-get update
RUN DEBIAN_FRONTEND=noninteractive apt-get install -y -q python-all python-pip 
ADD ./StormClustering/requirements.txt /tmp/requirements.txt
RUN pip install -qr /tmp/requirements.txt
ADD ./StormClustering /opt/StormClustering/
WORKDIR /opt/StormClustering
EXPOSE 8000
CMD ["python", "StormClustering.py"]

