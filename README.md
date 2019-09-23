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


to check for an alias and get ensembl id for that gene:
```
smembers gene:HYST2477
```
To get data for 1 ensembl id
``` 
smembers id:ENSG00000121410
```


### To run etl 
#### geneInfo
To compile and build runtime:
```
mvn clean install
```
To execute etl with geneInfo (populate Redis with the geneInfo file (gene alias and gene info):
```
java -jar target/geneset-etl-1.0-SNAPSHOT-jar-with-dependencies.jar geneInfo Homo_sapiens.gene_info.txt
java -jar target/geneset-etl-1.0-SNAPSHOT-jar-with-dependencies.jar hpo ALL_SOURCES_ALL_FREQUENCIES_genes_to_phenotype.txt
java -jar target/geneset-etl-1.0-SNAPSHOT-jar-with-dependencies.jar orpha en_product6.xml

```

###  Source of Data

#### HPO - Human Phenotype Ontology

http://compbio.charite.de/jenkins/job/hpo.annotations.monthly/lastSuccessfulBuild/artifact/annotation/ALL_SOURCES_ALL_FREQUENCIES_genes_to_phenotype.txt

#### Orphanet
##### RARE DISEASES WITH THEIR ASSOCIATED GENES:

http://www.orphadata.org/cgi-bin/index.php

#### Radboudumc

- Download all genes in PDF format
https://issuu.com/radboudumc/docs/genelist_dg216_nijmegen?fr=sMDA4NzU2NTY
- convert PDF to excel
https://smallpdf.com/fr/pdf-en-excel
- Convert to CSV (tab delimited)
Note: There is 6 tab to be saved as

