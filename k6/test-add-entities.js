import {DEFAULT_PARAMS, DEFAULT_TRIGGER_TIME, HOST, post, SUCCESS_RESPONSE_CODES} from "./commons.js";
import {sleep} from 'k6';

export function addStats() {
    return {
        'is add status OK': (r) => SUCCESS_RESPONSE_CODES.includes(r.status),
        'is add OK': (r) => r.body !== null,
        'is add response time less than 100ms': (r) => r.timings.duration < DEFAULT_TRIGGER_TIME
    };
}

export default function () {
    const payload = JSON.stringify({
        name: 'Test Entity',
    });

    post(HOST, payload, DEFAULT_PARAMS, addStats());

    sleep(5);
}