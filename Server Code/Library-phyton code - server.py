"""Author: Iman Abu Hashish"""
#db_client.py

import os
import rethinkdb as r
from rethinkdb.errors import RqlRuntimeError, RqlDriverError

RDB_HOST = 'localhost'
RDB_PORT = 28015

PROJECT_DB = 'LibraryDB'
PROJECT_TABLE = 'log'

db_connection = r.connect(RDB_HOST, RDB_PORT)

def db_setup():
    
    try:
        r.db_create(PROJECT_DB).run(db_connection)
        print ('Database setup completed.')
    
    except RqlRuntimeError:
        try:
            r.db(PROJECT_DB).table_create(PROJECT_TABLE).run(db_connection)
            print ('Table creation completed')
        
        except:
            print ('Table already exists.Nothing to do')
            
db_setup()

import falcon
import json
from waitress import serve
import codecs
import re


class LibraryTableResource:
     
    def on_get(self, req, resp):

        if req.get_param("id"):
            result = r.db(PROJECT_DB).table('library').get(req.get_param("id")).run(db_connection)
        else:
            library_cursor = r.db(PROJECT_DB).table('library').run(db_connection)
            result = [i for i in library_cursor]
        resp.body = json.dumps(result)
    
    def on_post(self, req, resp):
        
        try:
            raw_json = req.stream
        except Exception as ex:
            raise falcon.HTTPError(falcon.HTTP_400,'Error',ex.message)
        
        try:
            reader = codecs.getreader("utf-8")
            result = json.load(reader(raw_json))
            sid =  r.db(PROJECT_DB).table('library').insert({'codice_biblioteca':result['codice_biblioteca'], 
                                                             'name':result['name'], 'telephone':result['telephone'],
                                                             'email':result['email'], 'address':result['address'],
                                                             'latitude':result['latitude'], 'longitude':result['longitude'],
                                                             'ia_id':result['ia_id']}).run(db_connection)
            resp.body = '%s'%sid
            
        except ValueError:
            raise falcon.HTTPError(falcon.HTTP_400,'Invalid JSON',
                                   'Could not decode the request body. The ''JSON was incorrect.')

api = falcon.API()
api.add_route('/library', LibraryTableResource())
serve(api, host='0.0.0.0', port=2020)
