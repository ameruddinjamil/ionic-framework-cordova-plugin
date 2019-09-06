var exec = require('cordova/exec');

var hotelLister = {
  // tag::query-hotels[]
  queryHotels: function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'HotelLister', 'queryHotels');
  },
  addUser: function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'HotelLister', 'addUser');
  },
  getUser: function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'HotelLister', 'getUser');
  },
  // end::query-hotels[]
};

module.exports = hotelLister;