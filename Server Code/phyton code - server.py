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

#Lists all available databases
#r.db_list().run(db_connection)

#Drops the specified database
#r.db_drop('Library').run(db_connection)

#List all available tables in the specified database
#r.db('LibraryDB').table_list().run(db_connection)

#Drop a table in a database
#r.db('LibraryDB').table_drop('log').run(db_connection)

#r.db('LibraryDB').table("book_availability").get("bcfa569c-68da-419f-8b8d-7ac657942d3c").delete().run(db_connection)

#One to many relations..
#r.db('LibraryDB').table("waypoint").eq_join("id", r.table("path")).run(db_connection)



#app.py

import falcon
import json
from waitress import serve
import codecs
import re

#from db_client import *

class ArmadioTableResource:
 
    def on_get(self, req, resp):

        #if req.get_param("id"):
        #    result = r.db(PROJECT_DB).table('armadio').get(req.get_param("id")).run(db_connection)
        
        if req.get_param("codice_topografico_completo"):
            result = r.db(PROJECT_DB).table('armadio').get(req.get_param("codice_topografico_completo")).run(db_connection)
        else:
            library_cursor = r.db(PROJECT_DB).table('armadio').run(db_connection)
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
            sid =  r.db(PROJECT_DB).table('armadio').insert({'number':result['number'],
                                                             'codice_topografico_completo':result['codice_topografico_completo'],
                                                                 'sezione_citta':result['sezione_citta'], 
                                                                 'sezione_ente':result['sezione_ente'],
                                                                 'sezione_biblioteca': result['sezione_biblioteca'], 
                                                                 'sezione_sede':result['sezione_sede'],
                                                                 'sezione_piano':result['sezione_piano'], 
                                                                 'sezione_locale':result['sezione_locale'],
                                                                 'sezione_armadio':result['sezione_armadio'], 
                                                                 'sezione_ripiano':result['sezione_ripiano'],
                                                                 'availability_id':result['availability_id'], 
                                                                 'library_id':result['library_id']}).run(db_connection)
            resp.body = '%s'%sid
          
        except ValueError:
            raise falcon.HTTPError(falcon.HTTP_400,'Invalid JSON',
                                   'Could not decode the request body. The ''JSON was incorrect.')
            
class ArmadioAvailabilityTableResource:
     
    def on_get(self, req, resp):

        if req.get_param("id"):
            result = r.db(PROJECT_DB).table('armadio_availability').get(req.get_param("id")).run(db_connection)
        else:
            library_cursor = r.db(PROJECT_DB).table('armadio_availability').run(db_connection)
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
            sid =  r.db(PROJECT_DB).table('armadio_availability').insert({'code':result['code'], 
                                                                          'description':result['description']}).run(db_connection)
            resp.body = '%s'%sid
            
        except ValueError:
            raise falcon.HTTPError(falcon.HTTP_400,'Invalid JSON',
                                   'Could not decode the request body. The ''JSON was incorrect.')
  
class BookTableResource:
    
    def on_get(self, req, resp):

        if req.get_param("id"):
            result = r.db(PROJECT_DB).table('book').get(req.get_param("id")).run(db_connection)
        else:
            library_cursor = r.db(PROJECT_DB).table('book').run(db_connection)
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
            sid =  r.db(PROJECT_DB).table('book').insert({'inventory':result['inventory'], 'title':result['title'], 
                                                                          'avaliability_id':result['availability_id'], 
                                                                          'library_id':result['library_id'], 
                                                                          'armadio_id':result['armadio_id'], 
                                                                          'serie_code':result['serie_code']}).run(db_connection)

            resp.body = '%s'%sid
            
        except ValueError:
            raise falcon.HTTPError(falcon.HTTP_400,'Invalid JSON',
                                   'Could not decode the request body. The ''JSON was incorrect.')
            
class BookAvailabilityTableResource:
     
    def on_get(self, req, resp):

        if req.get_param("id"):
            result = r.db(PROJECT_DB).table('book_availability').get(req.get_param("id")).run(db_connection)
        else:
            library_cursor = r.db(PROJECT_DB).table('book_availability').run(db_connection)
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

            sid = r.db(PROJECT_DB).table('book_availability').insert({'code':result['code'],
                                                                      'description':result['description']}).run(db_connection)       
            resp.body = '%s'%sid

        except ValueError:
            raise falcon.HTTPError(falcon.HTTP_400,'Invalid JSON',
                                   'Could not decode the request body. The ''JSON was incorrect.')
            
class ConfirmationTypeTableResource:
     
    def on_get(self, req, resp):

        if req.get_param("id"):
            result = r.db(PROJECT_DB).table('confirmation_type').get(req.get_param("id")).run(db_connection)
        else:
            library_cursor = r.db(PROJECT_DB).table('confirmation_type').run(db_connection)
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
            sid =  r.db(PROJECT_DB).table('confirmation_type').insert({'code':result['code'], 
                                                                          'description':result['description']}).run(db_connection)

            resp.body = '%s'%sid
            
        except ValueError:
            raise falcon.HTTPError(falcon.HTTP_400,'Invalid JSON',
                                   'Could not decode the request body. The ''JSON was incorrect.')
            
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
            
class MapTableResource:
     
    def on_get(self, req, resp):

        if req.get_param("id"):
            result = r.db(PROJECT_DB).table('map').get(req.get_param("id")).run(db_connection)
        else:
            library_cursor = r.db(PROJECT_DB).table('map').run(db_connection)
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
            sid =  r.db(PROJECT_DB).table('map').insert({'name':result['name'], 'description':result['description'],
                                                             'path':result['path'], 'contained_by':result['contained_by'],
                                                             'library_id':result['library_id']}).run(db_connection)
            resp.body = '%s'%sid

        except ValueError:
            raise falcon.HTTPError(falcon.HTTP_400,'Invalid JSON',
                                   'Could not decode the request body. The ''JSON was incorrect.')

class OperationTypeTableResource:
     
    def on_get(self, req, resp):

        if req.get_param("id"):
            result = r.db(PROJECT_DB).table('operation_type').get(req.get_param("id")).run(db_connection)
        else:
            library_cursor = r.db(PROJECT_DB).table('operation_type').run(db_connection)
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
            sid =  r.db(PROJECT_DB).table('operation_type').insert({'code':result['code'], 
                                                                          'description':result['description']}).run(db_connection)
            resp.body = '%s'%sid

        except ValueError:
            raise falcon.HTTPError(falcon.HTTP_400,'Invalid JSON',
                                   'Could not decode the request body. The ''JSON was incorrect.')
            
class PathTableResource:
     
    def on_get(self, req, resp):

        if req.get_param("id"):
            result = r.db(PROJECT_DB).table('path').get(req.get_param("id")).run(db_connection)
        else:
            library_cursor = r.db(PROJECT_DB).table('path').run(db_connection)
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
            sid =  r.db(PROJECT_DB).table('path').insert({'source_wp_id':result['source_wp_id'],
                                                          'target_wp_id':result['target_wp_id'], 
                                                          'distance':result['distance']}).run(db_connection)
            
            resp.body = '%s'%sid

        except ValueError:
            raise falcon.HTTPError(falcon.HTTP_400,'Invalid JSON',
                                   'Could not decode the request body. The ''JSON was incorrect.')
            
class WaypointTableResource:
     
    def on_get(self, req, resp):

        if req.get_param("id"):
            result = r.db(PROJECT_DB).table('waypoint').get(req.get_param("id")).run(db_connection)
        else:
            library_cursor = r.db(PROJECT_DB).table('waypoint').run(db_connection)
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
            sid =  r.db(PROJECT_DB).table('waypoint').insert({'latitude':result['latitude'],
                                                              'longitude':result['longitude'],
                                                              'name':result['name'], 'armadio_id':result['armadio_id'],
                                                              'library_id':result['library_id']}).run(db_connection)
            resp.body = '%s'%sid

        except ValueError:
            raise falcon.HTTPError(falcon.HTTP_400,'Invalid JSON',
                                   'Could not decode the request body. The ''JSON was incorrect.')

class LogTableResource():
    
    def on_get(self, req, resp):

        if req.get_param("id"):
            result = r.db(PROJECT_DB).table('log').get(req.get_param("id")).run(db_connection)
        else:
            library_cursor = r.db(PROJECT_DB).table('log').run(db_connection)
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
            sid =  r.db(PROJECT_DB).table('log').insert({'operation_code':result['operation_code'],
                                                         'confirmation_code':result['confirmation_code'],
                                                         'request_timestamp':result['request_timestamp'],
                                                         'confirmation_timestamp':result['confirmation_timestamp'],
                                                         'start_nav_timestamp':result['start_nav_timestamp'],
                                                         'end_nav_timestamp':result['end_nav_timestamp'], 
                                                         'book_id':result['book_id'], 'library_id':result['library_id'],
                                                         'armadio_id':result['armadio_id']}).run(db_connection)
            resp.body = '%s'%sid

        except ValueError:
            raise falcon.HTTPError(falcon.HTTP_400,'Invalid JSON',
                                   'Could not decode the request body. The ''JSON was incorrect.')           
            
class StaticResource(object):
    def on_get(self, req, resp):
        resp.status = falcon.HTTP_200
        resp.content_type = 'text/html'
        with open('C:\\Users\\i\\Desktop\\index.html', 'r') as f:
            resp.body = f.read()
            
            
api = falcon.API()

#GET and POST routes..
api.add_route('/armadio', ArmadioTableResource())
api.add_route('/armadio_availability', ArmadioAvailabilityTableResource())
api.add_route('/book', BookTableResource())
api.add_route('/book_availability', BookAvailabilityTableResource())
api.add_route('/confirmation_type', ConfirmationTypeTableResource())
api.add_route('/library', LibraryTableResource())
api.add_route('/map', MapTableResource())
api.add_route('/operation_type', OperationTypeTableResource())
api.add_route('/path', PathTableResource())
api.add_route('/waypoint', WaypointTableResource())
api.add_route('/log', LogTableResource())

#HTML Page for deep linking route
api.add_route('/', StaticResource())



serve(api, host='0.0.0.0', port=2020)
