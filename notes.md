# Assignment Notes

## Cassandra to Kubernetes mapping

```
Cassandra      <--->      Kubernetes
Datacenter     <--->      Node/Cluster
Ring           <--->      StatefulSet
Node           <--->      Pod/Container
```

## Useful commands

- enter shell in cassandra container: `kubectl exec --stdin --tty cassandra-0 -- /bin/bash`
- view ring status: `nodetool status` on cassandra node
- decommission a cassandra node: `nodetool decommission` (see [cassandra docs](https://cassandra.apache.org/doc/latest/cassandra/operating/topo_changes.html))
- view logs: `kubectl logs -f cassandra-1`
- run stress test: `kubectl exec experiment-controller-0 -- bash bin/stress.sh --duration 400 500:1500:1000`
- enable autoscaler: `kubectl autoscale statefulset cassandra  --cpu-percent=50 --min=1 --max=10`
- view autoscaler status: `kubectl get hpa cassandra --watch`
- upgrade helm chart: `helm upgrade cassandra-cluster-1678802795 ${k8_scalar_dir}/operations/cassandra-cluster`
  - see `helm list` to find right name

## Problems and Solutions

- no persistent volumes? -> cassandra nodes fail to rejoin the ring if the crash
  - persistent volumes allow them to join very quickly
- downscaling kills node: cassandra ring in inconsistent state!
  - solution: terminate gracefully (see updated helm chart), `nodetool decommission`
- drawbacks of graceful termination
  - very slow, scaling halts the database, not ideal
  - additional problem: when scaling back up, cassandra node may have outdated view of the
    ring and try to contact decommissioned nodes.
      - solution: remove persistent volume after decommissioning node
        - new problem: don't want to remove all data when scaling down to 0
          - make sure at least one node is running at all that holds the data