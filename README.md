# K8-scalar
K8-scalar is a workbench for implementing and evaluating different self-adaptive
approaches to autoscaling container-orchestrated services. 

The k8-scalar artifact has been used and validated in the context of
autoscalers for database clusters. Although autoscalers for database
clusters  or multi-tier applications  have been researched, developing
an effective autoscaler for databases is still an art, rather than a
science. 

# Cassandra Resilience

## Deploying cass-operator and using k8-scalar

To deploy cass-operator, run the following commands from the root directory of this repository.

1. Install dependency of cass-operator: `helm install cert-manager jetstack/cert-manager --namespace cert-manager --create-namespace --set installCRDs=true`
2. Install a CassandraDatacenter: `helm install cass-dc operations/cass-operator-dc --namespace cass-operator --create-namespace`
3. For unknown reasons, the initial installation does not properly start the datacenter, but reapplying the CassandraDatacenter YAML solves the issue: `helm upgrade -n cass-operator cass-dc operations/cass-operator-dc`

## Deploying k8-scalar

To deploy the monitoring core and k8-scalar experiment controller, run the following commands.

1. Remove the existing clusterrole: 
```
role=kubectl get clusterrole | grep heapster | head -n1 | awk '{print $1;}'
kubectl delete clusterrole $role
```
2. Install monitoring core: `helm install operations/monitoring-core --generate-name --namespace=kube-system`
3. Install experiment controller: `helm install experiment-controller operations/experiment-controller`

## Running experiments

To set up Cassandra for an experiment, create a keyspace and table with the desired replication factor.

- `kubectl exec -n cass-operator cluster1-dc1-default-sts-0    -- cqlsh -e "CREATE KEYSPACE IF NOT EXISTS scalar WITH replication = {'class':'SimpleStrategy', 'replication_factor':2};"`
- `kubectl exec -n cass-operator cluster1-dc1-default-sts-0    -- cqlsh -e "CREATE TABLE IF NOT EXISTS scalar.logs (id text PRIMARY KEY, timestamp text, message text);"`

As an alternative to creating a keyspace and table manually, we experimented with creating a pre-start k8-scalar User called [CassandraAdminInitUser](https://github.com/Danacus/k8-scalar/blob/master/development/scalar/src/be/kuleuven/distrinet/scalar/users/CassandraAdminInitUser.java).

To run an experiment, shell into the experiment controller with `kubectl exec -it experiment-controller-0 -- bash`. 
Then configure `experiment.properties`, and start `scalar.jar` with `java -jar target/scalar-1.0.0.jar etc/platform.properties etc/experiment.properties`.

After completing an experiment, the keyspace should be removed before starting a new experiment: `kubectl exec -n cass-operator cluster1-dc1-default-sts-2    -- cqlsh -e "DROP KEYSPACE scalar;"`.


# Relevant publications

  * Eddy Truyen, Wito Delnat, Ansar Rafique, Dimitri Van Landuyt, Wouter Joosen, "K8-Scalar: A workbench to compare autoscalers for
container-orchestrated database clusters", in Proceedings of the 13th International Symposium on Software Engineering for Adaptive and Self-Managing Systems (SEAMS 2018), Gothenburg, Sweden. ([pdf](./docs/seams2018_CR.pdf)).

# Documentation

* [Tutorial](./docs/tutorial.md)
* [Overview](./docs/overview.md)
* [Scalar](./docs/scalar/features.md)
* [Using K8-Scalar with other auto-scalers](./docs/tutorial.md#iv-development)

# Help
* If you experience troubles with setting up k8-scalar, send me a message at [slack](https://k8-scalar.slack.com). In case of a bug or feature request, add an [issue](https://github.com/k8-scalar/k8-scalar/issues).
