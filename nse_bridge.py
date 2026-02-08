from flask import Flask, jsonify, request
from nsepython import nse_optionchain_scrapper
import json

app = Flask(__name__)

@app.route('/getOptionChain', methods=['GET'])
def get_option_chain():
    symbol = request.args.get('symbol', 'NIFTY')
    try:
        data = nse_optionchain_scrapper(symbol)
        # nsepython returns a dictionary that matches the NSE format
        return jsonify(data)
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/fetchNSEData', methods=['GET'])
def fetch_nse_data():
    symbol = request.args.get('symbol', 'NIFTY')
    try:
        data = nse_optionchain_scrapper(symbol)
        # Extract underlying value for spot price
        spot_price = data['records']['underlyingValue']
        expiry_date = data['records']['expiryDates'][0]
        
        # Simplified response for NiftyData
        # In a real scenario, you'd calculate Greeks/IV here
        response = {
            "spotPrice": spot_price,
            "expiryDate": expiry_date,
            "currentIV": 15.0, # Placeholder
            "atmStrike": round(spot_price / 50) * 50,
            "callPrice": 100.0, # Placeholder
            "putPrice": 100.0, # Placeholder
            "straddlePrice": 200.0, # Placeholder
            "theta": -10.0, # Placeholder
            "gamma": 0.002, # Placeholder
            "oiChange": 1.5, # Placeholder
            "volume": 500000.0, # Placeholder
            "bidAskSpread": 0.05, # Placeholder
            "timestamp": int(data['records']['timestamp'] or 0) # Simplified
        }
        return jsonify(response)
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    # Run on all interfaces so local Android device can connect via IP
    app.run(host='0.0.0.0', port=5000)
