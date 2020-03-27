# clin-geneset-etl

Populate Redis table with scala

### Redis docker-compose

#### Installation
on a swarm cluster or on a local environment
Create a network instance with 

```docker network create -d overlay --attachable proxy```
Then Run 

```docker-compose up```

To access the Redis instance
on another console

Run:
```
docker exec -ti redis_redis_1 /bin/sh
redis-cli
> ping
the redis should answer PONG
```
To clean Redis run the following command on the cli:
```
flushall
```

to check for an alias and get ensembl id for that gene:
```
smembers gene:HYST2477
```
To get data for 1 ensembl id
``` 
smembers id:ENSG00000121410
```


### To run etl
#### To compile and build runtime:
```
mvn clean install
``` 
#### Step 1 geneInfo

To execute etl with geneInfo (populate Redis with the geneInfo file (gene alias and gene info):
```
java -jar target/geneset-etl-1.0-SNAPSHOT-jar-with-dependencies.jar geneInfo Homo_sapiens.gene_info.txt
```
#### Step 2 HPO
To execute etl with hpo (populate Redis with the HPO file (Gene to HPO panel)
```
java -jar target/geneset-etl-1.0-SNAPSHOT-jar-with-dependencies.jar hpo ALL_SOURCES_ALL_FREQUENCIES_genes_to_phenotype.txt
```
#### Step 3 Orphanet
To execute etl with orphanets (populate Redis with the orphanets xml file (ensembl to orphanet panel)
```
java -jar target/geneset-etl-1.0-SNAPSHOT-jar-with-dependencies.jar orpha en_product6.xml
```
#### Step 4 Radboudumc
To execute etl with radboudumc (populate Redis with the radboudumc genes panels files in pdf (Gene to Radboudumc panel)
```
java -jar target/geneset-etl-1.0-SNAPSHOT-jar-with-dependencies.jar rad RAD_Files _DG217.pdf
```
###  Source of Data

#### NCBI
ftp://ftp.ncbi.nih.gov/gene/DATA/GENE_INFO/Mammalia/Homo_sapiens.gene_info.gz

#### HPO - Human Phenotype Ontology

http://compbio.charite.de/jenkins/job/hpo.annotations.monthly/lastSuccessfulBuild/artifact/annotation/ALL_SOURCES_ALL_FREQUENCIES_genes_to_phenotype.txt

#### Orphanet
##### RARE DISEASES WITH THEIR ASSOCIATED GENES:

http://www.orphadata.org/cgi-bin/index.php

#### Radboudumc

- Download all genes panels in PDF format
https://www.radboudumc.nl/en/patientenzorg/onderzoeken/exome-sequencing-diagnostics/information-for-referrers/exome-panels

#### note: (Feb 2020) 3 new panels were added
##### Liver, SHHM, Hereditary Bone Marrow Failure 
##### on Rad website, when we click on Muscle disorders, the Intelectual disorders shown instead... So, I'm running with a copy of 216 instead...
