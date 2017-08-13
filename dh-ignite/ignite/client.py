# -*- coding:utf-8 -*-
from collections import OrderedDict
from pprint import pprint

import requests


class IgniteClient:
    END_POINT = 'http://{host}:{port}/{context_path}'
    cmd_mapping = {
        "version": "version",
        "log": "log",
        "decrement": "decr",
        "increment": "incr"
    }

    def __init__(self, host='127.0.0.1', port=8080, context_path='ignite'):
        """
        Apache Ignite Python Client
        :param host: ignite cluster or node host 
        :param port: ignite port
        :param context_path: api context path
        """
        self._host = host
        self._port = port
        self._end_point = self.END_POINT.format(host=self._host,
                                                port=self._port, context_path=context_path)

    def _build_req_params(self, command, req_params={}):
        req = OrderedDict()
        req.update(req_params)
        req['cmd'] = self.cmd_mapping.get(command, command)
        return req

    def _request(self, command, req_params={}):
        return requests.get(self._end_point,
                            params=self._build_req_params(command, req_params)).json()

    def _make_cache_keys_params(self, params={}, param_key='key'):
        inx = 1
        result = {}
        for param in params.items():
            if param.lower() != 'cache_name':
                result[param_key + str(inx)] = param
                inx += 1
        return result

    def _make_cache_kv_params(self, params={}):
        inx = 1

        result = {}
        for param, val in params.items():
            if param.lower() != 'cache_name':
                result["k" + str(inx)] = param
                result["v" + str(inx)] = val
                inx += 1
        return result

    def log(self, from_=None, to_=None, path=None):
        """
        ignite log
        :param from_: 
        :param to_: 
        :param path: 
        :return: 
        """
        req = {
            "from": from_,
            "to": to_,
            "path": path
        }
        return beatify_ignite_response(self._request("log", req_params=req))

    def version(self):
        return beatify_ignite_response(self._request("log", req_params={}))

    def decrement(self, key, delta, cache_name=None, init=None):
        return beatify_ignite_response(self._request("decr",
                                                     req_params=dict(key=key,
                                                                     delta=delta,
                                                                     cache_name=cache_name,
                                                                     init=init)))

    def decr(self, key, delta, cache_name=None, init=None):
        return self.decrement(key, delta, cache_name, init)

    def increment(self, key, delta, cache_name=None, init=None):
        return beatify_ignite_response(
            self._request('incr', dict(key=key, delta=delta, cache_name=cache_name, init=init)))

    def incr(self, key, delta, cache_name=None, init=None):
        return self.increment(key, delta, cache_name, init)

    def cache(self, cache_name=None, dest_id=None):
        return beatify_ignite_response(self._request('cache',
                                                     dict(cacheName=cache_name,
                                                          destId=dest_id)))

    def cache_metrics(self, cache_name=None, dest_id=None):
        return self.cache(cache_name, dest_id)

    def cas(self, key, val, val2, cache_name=None, dest_id=None):
        return beatify_ignite_response(self._request('cas', dict(key=key, val=val,
                                                                 val2=val2,
                                                                 cacheName=cache_name, destId=dest_id)))

    def compare_and_swap(self, key, val, val2, cache_name=None, dest_id=None):
        return self.cas(key, val, val2, cache_name, dest_id)

    def prepend(self, key, val, cache_name=None, dest_id=None):
        return beatify_ignite_response(
            self._request('prepend', dict(key=key, val=val, cacheName=cache_name, destId=dest_id)))

    def append(self, key, val, cache_name=None, dest_id=None):
        return beatify_ignite_response(
            self._request('append', dict(key=key, val=val, cacheName=cache_name, destId=dest_id)))

    def replace(self, key, val, cache_name=None, dest_id=None):
        return beatify_ignite_response(
            self._request('rep', dict(key=key, val=val, cacheName=cache_name, destId=dest_id)))

    def rep(self, key, val, cache_name=None, dest_id=None):
        return self.replace(key, val, cache_name, dest_id)

    def getrep(self, key, val, cache_name=None, dest_id=None):
        return beatify_ignite_response(
            self._request('getrep', dict(key=key, val=val, cacheName=cache_name, destId=dest_id)))

    def get_and_replace(self, key, val, cache_name=None, dest_id=None):
        return self.getrep(key, val, cache_name, dest_id)

    def repval(self, key, val, val2, cache_name=None, dest_id=None):
        return beatify_ignite_response(
            self._request('repval', dict(key=key, val=val, val2=val2, cacheName=cache_name, destId=dest_id)))

    def replace_value(self, key, val, val2, cache_name=None, dest_id=None):
        return self.repval(key, val, val2, cache_name, dest_id)

    # todo to handle different stuff
    def rmvall(self, **kwargs):
        return beatify_ignite_response(self._request('rmvall', self._make_cache_keys_params(kwargs)))

    def remove_all(self, **kwargs):
        return self.rmvall(**kwargs)

    def rmvval(self, key, val, cache_name=None, dest_id=None):
        return beatify_ignite_response(
            self._request('rmvval', dict(key=key, val=val, cacheName=cache_name, destId=dest_id)))

    def remove_value(self, key, val, cache_name=None, dest_id=None):
        return self.rmvval(key, val, cache_name, dest_id)

    def rmv(self, key, cache_name=None, dest_id=None):
        return beatify_ignite_response(self._request('rmv', dict(key=key, cacheName=cache_name, destId=dest_id)))

    def remove(self, key, cache_name=None, dest_id=None):
        return self.rmv(key, cache_name, dest_id)

    def getrmv(self, key, cache_name=None, dest_id=None):
        return beatify_ignite_response(self._request('getrmv', dict(key=key, cacheName=cache_name, destId=dest_id)))

    def get_and_remove(self, key, cache_name=None, dest_id=None):
        return self.getrmv(key, cache_name, dest_id)

    def add(self, key, val, cache_name=None, dest_id=None):
        return beatify_ignite_response(
            self._request('add', dict(key=key, val=val, cacheName=cache_name, destId=dest_id)))

    def putall(self, cache_name, cache_object={}):
        params = {}
        params['cacheName'] = cache_name
        params.update(self._make_cache_kv_params(cache_object))
        return beatify_ignite_response(self._request('putall',
                                                     params))

    def put(self, key, val, cache_name=None, dest_id=None):
        return beatify_ignite_response(
            self._request('put', dict(key=key, val=val, cacheName=cache_name, destId=dest_id)))

    def getall(self, **kwargs):
        return beatify_ignite_response(self._request('getall', self._make_cache_keys_params(kwargs)))

    def get(self, key, cache_name=None, dest_id=None):
        return beatify_ignite_response(self._request('get', dict(key=key, cacheName=cache_name, destId=dest_id)))

    def conkey(self, key, cache_name=None, dest_id=None):
        return beatify_ignite_response(self._request('conkey',
                                                     dict(key=key, cacheName=cache_name, destId=dest_id)))

    def contains_key(self, key, cache_name=None, dest_id=None):
        return self.conkey(key, cache_name, dest_id)

    def conkeys(self, cache_name=None, dest_id=None, **kwargs):
        params = dict(cache_name=cache_name, dest_id=dest_id)
        params.update(self._make_cache_keys_params(kwargs))
        return beatify_ignite_response(self._request('conkeys', params))

    def contains_keys(self, **kwargs):
        return self.conkeys(**kwargs)

    def getput(self, key, val, cache_name=None, dest_id=None):
        return beatify_ignite_response(
            self._request('getput', dict(key=key, val=val, cacheName=cache_name, destId=dest_id)))

    def get_and_put(self, key, val, cache_name=None, dest_id=None):
        return self.getput(key, val, cache_name, dest_id)

    def putifabs(self, key, val, cache_name=None, dest_id=None):
        return beatify_ignite_response(
            self._request('putifabs', dict(key=key, val=val, cacheName=cache_name, destId=dest_id)))

    def put_if_absent(self, key, val, cache_name=None, dest_id=None):
        return self.putifabs(key, val, cache_name, dest_id)

    def getputifabs(self, key, val, cache_name=None, dest_id=None):
        return beatify_ignite_response(
            self._request('getputifabs', dict(key=key, val=val, cacheName=cache_name, destId=dest_id)))

    def get_and_put_if_absent(self, key, val, cache_name=None, dest_id=None):
        return self.getputifabs(key, val, cache_name, dest_id)

    def size(self, cache_name=None):
        return beatify_ignite_response(self._request('size', dict(cacheName=cache_name)))

    def cache_size(self, cache_name=None):
        return self.size(cache_name)

    def metadata(self, cache_name=None):
        return beatify_ignite_response(self._request('metadata', dict(cacheName=cache_name)))

    def cache_metadata(self, cache_name=None):
        return self.metadata(cache_name)

    def getorcreate(self, cache_name=None):
        return beatify_ignite_response(self._request('getorcreate', dict(cacheName=cache_name)))

    def get_or_create_cache(self, cache_name=None):
        return self.getorcreate(cache_name)

    def destcache(self, cache_name=None):
        return beatify_ignite_response(self._request('destcache', dict(caceName=cache_name)))

    def destroy_cache(self, cache_name=None):
        return self.destcache(cache_name)

    def node(self, ip=None, id=None, mtr=False, attr=False):
        return beatify_ignite_response(self._request('node', dict(ip=ip, id=id, mtr=mtr, attr=attr)))

    def top(self, ip=None, id=None, mtr=False, attr=False):
        return beatify_ignite_response(self._request('top', dict(ip=ip, id=id, mtr=mtr, attr=attr)))

    def topology(self, ip=None, id=None, mtr=False, attr=False):
        return self.top(ip, id, mtr, attr)

    def exe(self, name, **kwargs):
        return beatify_ignite_response(self._request('exe', kwargs))

    def execute(self, name, **kwargs):
        return self.exe(name, **kwargs)

    def res(self, id):
        return beatify_ignite_response(self._request('res', dict(id=id)))

    def result(self, id):
        return self.res(id)

    def qryexe(self, cache_name, qry, type, page_size, **kwargs):
        params = dict(cache_name=cache_name, qry=qry,
                      type=type, pageSize=page_size)
        params.update(self._make_cache_keys_params(kwargs, 'arg'))

        return beatify_ignite_response(self._request('qryexe', params))

    def sql_query_execute(self, qry, type, page_size, **kwargs):
        return self.qryexe(qry, type, page_size, **kwargs)

    def qryfldexe(self, qry, page_size, cache_name=None, **kwargs):
        params = dict(cache_name=cache_name, qry=qry, pageSize=page_size)
        params.update(self._make_cache_keys_params(kwargs, 'arg'))
        return beatify_ignite_response(self._request('qryfldexe', kwargs))

    def sql_fields_query_execute(self, qry, page_size, cache_name=None, **kwargs):
        return self.qryfldexe(qry, page_size, cache_name=cache_name, **kwargs)

    def qryscanexe(self, page_size, cache_name, class_name=None):
        return beatify_ignite_response(
            self._request('qryscanexe', dict(pageSize=page_size, cacheName=cache_name, className=class_name)))

    def sql_scan_query_execute(self, page_size, cache_name, class_name=None):
        return self.qryscanexe(page_size, cache_name, class_name)

    def qryfetch(self, page_size, qry_id):
        return beatify_ignite_response(self._request('qryfetch', dict(pageSize=page_size, qryId=qry_id)))

    def sql_query_fetch(self, page_size, qry_id):
        return self.qryfetch(page_size, qry_id)

    def qrycls(self, qry_id):
        return beatify_ignite_response(self._request('qrycls', dict(qryId=qry_id)))

    def sql_query_close(self, qry_id):
        return self.qrycls(qry_id)


def beatify_ignite_response(resp):
    resp_json = resp.get('response', '')
    affinity_node_id = resp.get('affinityNodeId', '')
    error = resp.get('error', 'No-Error')
    session_token = resp.get('sessionToken', '')
    success_status = resp.get('successStatus', '')
    status_mapping = {
        0: 'Success, error is {msg}!',
        1: 'Ignite Failed - {msg}',
        2: 'Authorization Failed - {msg}',
        3: 'Security Check Failed - {msg}',
    }

    pprint(status_mapping.get(success_status).format(msg=error), width=100)
    pprint(resp, width=100)
    return resp

#
# if __name__ == '__main__':
#     # response = IgniteClient(host="10.199.212.83").node(id="87082788-1b3e-45d2-ad7d-2e0a7d9f3ac7",ip="10.199.212.83")
#     back_list = {
#         "debt": "12342345",
#         "id": 1238990,
#         "reason": "test_reason"
#     }
#
#     # client = IgniteClient(host="10.199.212.83")
#     # client = IgniteClient(host="10.214.160.188")
#     client = IgniteClient(host="10.214.129.40")
#     response = client.node(ip="10.214.129.40")
#     print(response)
#     # response = client.getorcreate("URCP-BLACKLIST")
#     # response = client.cache_metadata()
    # response = client.cache(cache_name="URCP-BLACKLIST")
    # response = client.add(cache_name="URCP-BLACKLIST", key=id, val=123456)
    #
    # response = client.putall(cache_name="URCP-BLACKLIST", cache_object=back_list)
    # response = client.get(dest_id="87082788-1b3e-45d2-ad7d-2e0a7d9f3ac7",
    #                       cache_name="URCP-BLACKLIST", key="id")
    # print(response)
