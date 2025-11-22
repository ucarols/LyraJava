LOCATION="eastus2"
RG="rg-gs2025-cgl"
SERVER_NAME="sqlserver-gs2025-lyra"
USERNAME="lyra"
PASSWORD="GlobalSolution2025!"
DBNAME="lyradb"

# Resource Group nao criado pois ja existe (criado na materia de .NET)

az sql server create -l $LOCATION -g $RG -n $SERVER_NAME -u $USERNAME -p $PASSWORD --enable-public-network true

az sql db create -g $RG -s $SERVER_NAME -n $DBNAME --service-objective Basic --backup-storage-redundancy Local --zone-redundant false

az sql server firewall-rule create -g $RG -s $SERVER_NAME -n AllowAll --start-ip-address 0.0.0.0 --end-ip-address 255.255.255.255
