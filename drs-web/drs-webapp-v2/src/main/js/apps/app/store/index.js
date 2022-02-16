import thunkMiddleware from 'redux-thunk'
import { createLogger } from 'redux-logger'
import { applyMiddleware, compose, createStore } from 'redux'

import rootReducer from "../reducers/reducers";

import { createBrowserHistory } from 'history'
import { routerMiddleware } from 'connected-react-router'

const loggerMiddleware = createLogger()


const history = createBrowserHistory()

const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose

const store = createStore(
		  rootReducer(history),
		   composeEnhancer(
			  applyMiddleware(
			  routerMiddleware(history),
			    thunkMiddleware, // lets us dispatch() functions
			    loggerMiddleware // neat middleware that logs actions
			  )
		  )
		)


	

export { history ,store}
