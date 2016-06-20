(function() {
    'use strict';
    angular
        .module('petStoreApp')
        .factory('PhotoUrl', PhotoUrl);

    PhotoUrl.$inject = ['$resource'];

    function PhotoUrl ($resource) {
        var resourceUrl =  'api/photo-urls/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
