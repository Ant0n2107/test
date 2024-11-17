import http from 'k6/http';
import {Counter} from 'k6/metrics';
import {check} from 'k6';

export let HOST = 'http://localhost:8080/api/entities';

export let errors = new Counter('errors');

export const DEFAULT_PARAMS = {
    headers: {
        'Content-Type': 'application/json',
    }
};

export const DEFAULT_TRIGGER_TIME = 100;

export const SUCCESS_RESPONSE_CODES = [200, 201];

export const RESPONSE_STATUS_CHECK = {
    'is status OK': (r) => SUCCESS_RESPONSE_CODES.includes(r.status)
};

export function post(url, payload, params = DEFAULT_PARAMS, check_object = null) {
    let res = http.post(`${url}`, payload, createTagsParams(url, params));
    let checkRes;
    if (check_object) {
        checkRes = check(res, check_object)
    } else {
        checkRes = check(res, RESPONSE_STATUS_CHECK);
    }
    errors.add(!checkRes, {url});
    return res;
}


export function get(url, params = DEFAULT_PARAMS, check_object = null) {
    const tagName = url.split("?", 1);
    let tagsParams = Object.assign({}, params, {tags: {name: `${tagName}`}});
    let res = http.get(`${url}`, tagsParams);
    let checkRes;
    if (check_object) {
        checkRes = check(res, check_object)
    } else {
        checkRes = check(res, RESPONSE_STATUS_CHECK);
    }
    errors.add(!checkRes, {url});
    return res;
}

export function isPathParam(word) {
    if(word.toUpperCase()===word) {
        return true;
    }

    const length_before = word.length;
    const word_after = word.replace(/[0-9]*/g,"");
    const length_after = word_after.length;
    return (length_before - length_after) > 6;
}

export function createTagsParams(url, params) {
    let result = params;
    if (url && !params.tags) {
        var tagName = url.toString();
        const index = tagName.indexOf("?");
        if(index>0) {
            tagName = tagName.substring(0, index);
        }
        tagName = tagName.split("/").map(word=>{
            if(word.length<=3){
                return word;
            } else {
                if(isPathParam(word)) {
                    return "{}"
                }
                return word;
            }
        }).join("/");
        result = Object.assign({}, params, {tags: {name: `${tagName}`}});
    }
    return result;
}
